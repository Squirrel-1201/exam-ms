package exam.bus.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IExamRecordService;
import exam.bus.IExamService;
import exam.bus.IStuExamService;
import exam.common.enums.*;
import exam.common.errorcode.StuExamError;
import exam.common.vo.*;
import exam.dao.entity.*;
import exam.dao.mapper.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssm.common.entity.BaseEntity;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class StuExamServiceImpl implements IStuExamService {

    private ExamStudentMapper examStudentMapper;

    private ExamRecordMapper examRecordMapper;

    private UserMapper userMapper;

    private ExamMapper examMapper;

    private IExamRecordService examRecordService;

    private ExamRecordOptionMapper recordOptionMapper;

    private IExamService examService;

    /**
     * 考试计划列表
     *
     * @param vo
     * @param page
     * @return
     */
    @Override
    public IPage<StudentExamVO> planList(StudentExamVO vo, Page page) {
        //判断是否已过考试时间（修改考生考试状态）
        this.checkStuExamStatus(vo.getUserId());
        return this.examStudentMapper.planList(vo, page);
    }

    private void checkStuExamStatus(Long stuId) {
        List<ExamStudentEntity> list = this.examStudentMapper.selectList(new LambdaQueryWrapper<ExamStudentEntity>()
                .eq(ExamStudentEntity::getStuId, stuId)
                .in(ExamStudentEntity::getStatus, CollUtil.newArrayList(ExamStuStatusEnum.EXAM_ING, ExamStuStatusEnum.TO_BE_EXAM)));
        if (CollUtil.isEmpty(list)) {
            return;
        }
        //判断是否已过考试时间（修改考生考试状态）
        list.stream().forEach(this::checkStuExamStatus);
    }

    /**
     * 已结束的考试列表
     *
     * @param vo
     * @param page
     * @return
     */
    @Override
    public IPage<StudentExamVO> endList(StudentExamVO vo, Page page) {
        return this.examStudentMapper.endList(vo, page);
    }

    /**
     * 考试考试
     *
     * @param examId 试卷id
     * @param userId 用户id
     * @return
     */
    @Override
    @Transactional
    public StudentExamDetailVO startExam(String ip, Long examId, Long userId) {
        //试卷校验
        ExamEntity examEntity = this.examMapper.selectById(examId);
        StuExamError.EXAM_INFO_ERROR.notNull(examEntity);
        StuExamError.EXAM_STATUS_ERROR.isTrue(examEntity.getStatus() == ExamStatus.PUBLISHED);
        StuExamError.EXAM_TIME_NOT_YET_ERROR.isTrue(LocalDateTimeUtil.between(examEntity.getStartTime(), LocalDateTime.now()).toMinutes() >= 0);

        synchronized (this) {
            //考生试卷验证
            LambdaQueryWrapper<ExamStudentEntity> wrapper = new LambdaQueryWrapper<ExamStudentEntity>()
                    .eq(ExamStudentEntity::getExamId, examId)
                    .eq(ExamStudentEntity::getStuId, userId)
                    .eq(ExamStudentEntity::getStatus, ExamStuStatusEnum.TO_BE_EXAM);
            ExamStudentEntity examStudent = this.examStudentMapper.selectOne(wrapper);
            StuExamError.EXAM_INFO_ERROR.notNull(examStudent);

            //判断是否已过考试时间（修改考生考试状态）
            this.checkStuExamStatus(examStudent);

            //生成考生试卷
            this.examRecordService.generateStuExam(examId, userId);

            //修改考生试卷信息为考试中
            examStudent.setStatus(ExamStuStatusEnum.EXAM_ING)
                    .setExamIp(ip)
                    .setStartTime(LocalDateTime.now());
            this.examStudentMapper.updateById(examStudent);
        }

        return this.examResult(examId, userId);
    }

    /**
     * 继续考试
     *
     * @param examId 试卷id
     * @param userId 用户id
     * @return
     */
    @Override
    public StudentExamDetailVO continueExam(String ip, Long examId, Long userId) {
        LambdaQueryWrapper<ExamStudentEntity> wrapper = new LambdaQueryWrapper<ExamStudentEntity>()
                .eq(ExamStudentEntity::getExamId, examId)
                .eq(ExamStudentEntity::getStuId, userId)
                .eq(ExamStudentEntity::getStatus, ExamStuStatusEnum.EXAM_ING);
        ExamStudentEntity examStudent = this.examStudentMapper.selectOne(wrapper);
        StuExamError.EXAM_INFO_ERROR.notNull(examStudent);
        // 原有考试未设置IP的不进行防作弊处理
        if (Objects.nonNull(examStudent.getExamIp())) {
            ExamEntity examEntity = examMapper.selectById(examId);
            if (examEntity.getPreventionCheat() == EnableEnum.ENABLE) {
                StuExamError.EXAM_NOT_ALLOW_CHANGE_PC.isTrue(examStudent.getExamIp().equals(ip));
            }
        }

        //判断是否已过考试时间（修改考生考试状态）
        this.checkStuExamStatus(examStudent);

        return this.examResult(examId, userId);
    }

    /**
     * 考试结果
     *
     * @param examId 试卷id
     * @param userId 用户id
     * @return
     */
    @Override
    public StudentExamDetailVO examResult(Long examId, Long userId) {
        StudentExamDetailVO vo = new StudentExamDetailVO();
        this.getStuExam(examId, userId, vo);
        this.getStuExamQuestion(examId, userId, vo);
        return vo;
    }


    /**
     * 获取考生试卷信息
     *
     * @param examId 试卷id
     * @param userId 用户id
     * @return
     */
    private void getStuExam(Long examId, Long userId, StudentExamDetailVO vo) {

        ExamEntity examEntity = this.examMapper.selectById(examId);
        StuExamError.EXAM_INFO_ERROR.notNull(examEntity);

        UserEntity userEntity = this.userMapper.selectById(userId);
        StuExamError.STU_INFO_ERROR.notNull(userEntity);

        LambdaQueryWrapper<ExamStudentEntity> wrapper = new LambdaQueryWrapper<ExamStudentEntity>()
                .eq(ExamStudentEntity::getExamId, examId)
                .eq(ExamStudentEntity::getStuId, userId)
                .ne(ExamStudentEntity::getStatus, ExamStuStatusEnum.TO_BE_EXAM);
        ExamStudentEntity examStudent = this.examStudentMapper.selectOne(wrapper);
        StuExamError.EXAM_INFO_ERROR.notNull(examStudent);

        vo.setUsername(userEntity.getUsername())
                .setRealName(userEntity.getRealName())
                .setExamId(examEntity.getId())
                .setExamStatus(examEntity.getStatus())
                .setExamName(examEntity.getName())
                .setExamScore(examEntity.getScore())
                .setExamStuStatus(examStudent.getStatus());

        //考试中需要计算剩余时间
        if (examStudent.getStatus() == ExamStuStatusEnum.EXAM_ING) {
            LocalDateTime currentTime = LocalDateTimeUtil.now();
            //计算考试剩余时间=考试时长-(当前时间-开始考试时间)
            Long remainingSecond = (examEntity.getLastTime() * 60) - LocalDateTimeUtil.between(examStudent.getStartTime(), currentTime).getSeconds();
            //计算距离考试结束的时间=考试截止时间-当前时间
            Long endTime = LocalDateTimeUtil.between(currentTime, examEntity.getEndTime()).getSeconds();

            //考试剩余时间和距离考试结束时间的最小值为该考生所拥有的剩余考试时长
            remainingSecond = NumberUtil.min(remainingSecond, endTime);
            //考试剩余时间（秒）
            vo.setRemainingSecond(remainingSecond < 0L ? 0L : remainingSecond);
        }

        //只有已结束的试卷才可以看到分数
        if (examEntity.getStatus() == ExamStatus.END) {
            vo.setStuScore(examStudent.getTotalScore());
        }
        // 考生状态为(通过或未通过)状态也可以查看分数
        if (examStudent.getStatus() == ExamStuStatusEnum.PASS || examStudent.getStatus() == ExamStuStatusEnum.NOT_PASS) {
            vo.setStuScore(examStudent.getTotalScore());
        }
    }


    private void getStuExamQuestion(Long examId, Long userId, StudentExamDetailVO vo) {
        List<ExamRecordEntity> list = this.examRecordMapper
                .selectList(
                        new LambdaQueryWrapper<ExamRecordEntity>()
                                .eq(ExamRecordEntity::getStuId, userId)
                                .eq(ExamRecordEntity::getExamId, examId)
                                .orderByAsc(BaseEntity::getId));
        if (CollUtil.isEmpty(list)) {
            return;
        }
        final Map<Long, List<ExamRecordOptionEntity>> optionMap = this.getOptionMap(examId, userId);

        list.stream().forEach(question -> {
            StudentExamDetailVO.QuestionDetailVO seq = new StudentExamDetailVO.QuestionDetailVO()
                    .setTitle(question.getTitle())
                    .setScore(question.getScore())
                    .setType(question.getType())
                    .setStuAnswer(question.getStuAnswer())
                    .setRecordId(question.getId());
            //选项
            if (optionMap.containsKey(question.getId())) {
                seq.setOptions(optionMap.get(question.getId()).stream().map(option -> new StudentExamDetailVO.QuestionOptionDetailVO().setSerialNo(option.getSerialNo()).setOption(option.getTitle())).collect(Collectors.toList()));
            }
            //只有已结束的考试才可以看到分数、参考答案、答案解析、状态
            if (vo.getExamStatus() == ExamStatus.END) {
                seq.setStuScore(question.getFinalScore())
                        .setStatus(NumberUtil.isGreater(question.getFinalScore(), BigDecimal.ZERO) ? YNEnum.YES : YNEnum.NO)
                        .setAnswer(question.getAnswer())
                        .setAnalyse(question.getAnalyse());
            }
            vo.getList().add(seq);
        });

    }

    /**
     * 获取选项集合
     *
     * @param examId
     * @param userId
     * @return
     */
    private Map<Long, List<ExamRecordOptionEntity>> getOptionMap(Long examId, Long userId) {
        List<ExamRecordOptionEntity> options = this.recordOptionMapper.findByExamIdAndStuId(examId, userId);
        if (CollUtil.isEmpty(options)) {
            return MapUtil.newHashMap();
        }
        return options.stream().collect(Collectors.toMap(ExamRecordOptionEntity::getExamRecordId, ListUtil::toList, (o, n) -> (List<ExamRecordOptionEntity>) CollUtil.addAll(o, n)));
    }

    /**
     * 提交问题答案
     *
     * @param vo
     */
    @Override
    @Transactional
    public void submitAnswer(SubmitAnswerVO vo) {
        //判断试题正确性
        ExamRecordEntity entity = this.examRecordMapper.selectOne(new LambdaQueryWrapper<ExamRecordEntity>()
                .eq(ExamRecordEntity::getId, vo.getRecordId())
                .eq(ExamRecordEntity::getStuId, vo.getStuId())
                .eq(ExamRecordEntity::getExamId, vo.getExamId()));
        StuExamError.QUESTION_NOT_FUND.notNull(entity);

        //判断考生试卷状态是否为考试中
        ExamStudentEntity examStudent = this.validExamIng(vo.getExamId(), vo.getStuId());

        //判断是否已过考试时间（修改考生考试状态）
        this.checkStuExamStatus(examStudent);

        //保存考生答案
        entity.setStuAnswer(CharSequenceUtil.isNotBlank(vo.getAnswer()) ? vo.getAnswer() : null);
        this.examRecordMapper.updateById(entity);
    }

    private void checkStuExamStatus(ExamStudentEntity examStudent) {
        ExamEntity exam = this.examMapper.selectById(examStudent.getExamId());
        if (exam == null) {
            return;
        }
        if (exam.getStatus() != ExamStatus.PUBLISHED) {
            return;
        }
        if (examStudent.getStatus() == ExamStuStatusEnum.TO_BE_EXAM) {
            if (LocalDateTime.now().isAfter(exam.getEndTime())) {
                this.examStudentMapper.updateById(examStudent.setStatus(ExamStuStatusEnum.MISS_EXAM));
            }
        } else if (examStudent.getStatus() == ExamStuStatusEnum.EXAM_ING) {
            if (LocalDateTime.now().isAfter(exam.getEndTime())) {
                this.examStudentMapper.updateById(examStudent.setEndTime(exam.getEndTime()).setStatus(ExamStuStatusEnum.COMPLETE));
            } else if (LocalDateTime.now().isAfter(examStudent.getStartTime().plusMinutes(exam.getLastTime()))) {
                this.examStudentMapper.updateById(examStudent.setEndTime(LocalDateTime.now()).setStatus(ExamStuStatusEnum.COMPLETE));
            }
        }
    }

    /**
     * 判断考生试卷状态为考试中
     *
     * @param examId
     * @param stuId
     */
    private ExamStudentEntity validExamIng(Long examId, Long stuId) {
        //判断试卷状态
        ExamStudentEntity examStudent = this.examStudentMapper.selectOne(new LambdaQueryWrapper<ExamStudentEntity>()
                .eq(ExamStudentEntity::getStuId, stuId)
                .eq(ExamStudentEntity::getExamId, examId));
        StuExamError.EXAM_INFO_ERROR.notNull(examStudent);
        StuExamError.EXAM_STATUS_ERROR.isTrue(examStudent.getStatus() == ExamStuStatusEnum.EXAM_ING);
        return examStudent;
    }

    /**
     * 交卷
     *
     * @param vo
     */
    @Override
    @Transactional
    public String submitExam(SubmitExamVO vo) {
        ExamEntity entity = this.examMapper.selectById(vo.getExamId());
        StuExamError.QUESTION_NOT_FUND.notNull(entity);
        StuExamError.EXAM_INFO_ERROR.isTrue(entity.getStatus() == ExamStatus.PUBLISHED);

        log.info("考生交卷，试卷id：{},考生id：{},确认交卷结果:{}", vo.getExamId(), vo.getStuId(), vo.isConfirm());
        //判断试卷状态为考试中
        ExamStudentEntity examStudent = this.validExamIng(vo.getExamId(), vo.getStuId());

        //判断是否已经全部题做完
        this.validExamAllCompleted(vo);

        //修改考生试卷状态
        examStudent.setStatus(ExamStuStatusEnum.COMPLETE);
        examStudent.setEndTime(LocalDateTimeUtil.now());
        this.examStudentMapper.updateById(examStudent);
        log.info("考生交卷结束");

        return entity.getName();
    }


    /**
     * 自动交卷
     */
    @Override
    @Transactional
    public void autoSubmitExam() {
        log.debug("自动交卷方法执行开始..........");
        List<ExamEntity> list = this.examMapper.selectList(new LambdaQueryWrapper<ExamEntity>().eq(ExamEntity::getStatus, ExamStatus.PUBLISHED));
        if (CollUtil.isEmpty(list)) {
            log.debug("没有已发布的试卷，自动交卷方法执行结束..........");
            return;
        }
        list.stream().forEach(this::updateExamStatus);
        log.debug("自动交卷方法执行结束..........");
    }

    private void updateExamStatus(ExamEntity exam) {

        //当前时间-考试时长
        LocalDateTime time = LocalDateTimeUtil.now().minusMinutes(exam.getLastTime());


        //修改已超过考试时长的考生试卷为已完成
        int num = this.examStudentMapper.update(null, new LambdaUpdateWrapper<ExamStudentEntity>()
                .set(ExamStudentEntity::getStatus, ExamStuStatusEnum.COMPLETE)
                .set(ExamStudentEntity::getEndTime, exam.getEndTime())
                .eq(ExamStudentEntity::getExamId, exam.getId())
                .eq(ExamStudentEntity::getStatus, ExamStuStatusEnum.EXAM_ING)
                .lt(ExamStudentEntity::getStartTime, time));

        if (num > 0) {
            log.info("自动交卷，试卷id:{}，试卷名称:{},当前时间-考试时长={}", exam.getId(), exam.getName(), time);
        }

        if (LocalDateTime.now().compareTo(exam.getEndTime()) > 0) {
            log.info("已超过考试截止日期,试卷id:{},试卷名称:{}", exam.getId(), exam.getName());
            //如果当前时间大于考试截止日期，修改待考的考试为缺考
            this.examStudentMapper.update(null, new LambdaUpdateWrapper<ExamStudentEntity>()
                    .set(ExamStudentEntity::getStatus, ExamStuStatusEnum.MISS_EXAM)
                    .eq(ExamStudentEntity::getExamId, exam.getId())
                    .eq(ExamStudentEntity::getStatus, ExamStuStatusEnum.TO_BE_EXAM));
        }

        long count = this.examStudentMapper.selectCount(new LambdaQueryWrapper<ExamStudentEntity>().eq(ExamStudentEntity::getExamId, exam.getId())
                .in(ExamStudentEntity::getStatus, ListUtil.toList(ExamStuStatusEnum.TO_BE_EXAM, ExamStuStatusEnum.EXAM_ING)));
        //如果没有考试中和待考状态的考生表示所有考生均已交卷
        if (count == 0) {
            log.info("修改试卷【{}】状态为阅卷中....", exam);
            //考生考试状态没有（待考、考试中）的试卷；代表所有考生均已考试完成，修改试卷状态为阅卷中
            this.examMapper.update(null, new LambdaUpdateWrapper<ExamEntity>().set(ExamEntity::getStatus, ExamStatus.MARKING).eq(BaseEntity::getId, exam.getId()));
        }
    }

    /**
     * 判断试题是否已全部做完
     *
     * @param vo
     */
    private void validExamAllCompleted(SubmitExamVO vo) {
        if (vo.isConfirm()) {
            return;
        }
        long count = this.examRecordMapper.selectCount(new LambdaQueryWrapper<ExamRecordEntity>()
                .eq(ExamRecordEntity::getStuId, vo.getStuId())
                .eq(ExamRecordEntity::getExamId, vo.getExamId())
                .isNull(ExamRecordEntity::getAnswer));
        StuExamError.EXAM_NOT_ALL_COMPLETED.isTrue(count <= 0, count);
    }


    /**
     * 自动阅卷
     */
    @Transactional
    @Override
    public void autoSubmitMarking() {
        log.debug("自动阅卷开始.............");
        List<ExamEntity> list = this.examMapper.selectList(new LambdaQueryWrapper<ExamEntity>().eq(ExamEntity::getStatus, ExamStatus.MARKING).eq(ExamEntity::getAutoMarking, EnableEnum.ENABLE));
        if (CollUtil.isEmpty(list)) {
            log.debug("没有需要阅卷的试卷，自动阅卷结束........");
            return;
        }
        list.forEach(exam -> examService.marking(exam.getId()));
        log.debug("自动阅卷结束.............");
    }

    @Override
    public void autoEndExam() {
        log.debug("自动结束试卷开始.............");
        List<ExamEntity> list = this.examMapper.selectList(new LambdaQueryWrapper<ExamEntity>().le(ExamEntity::getEndTime, LocalDateTime.now()).ne(ExamEntity::getStatus, ExamStatus.END));
        if (CollUtil.isEmpty(list)) {
            log.debug("没有需要自动结束的试卷........");
            return;
        }
        list.stream().forEach(exam -> examService.autoEndExamStatus(exam.getId()));
        log.debug("已过考试截止日期的试卷自动结束考试.............");
    }


}
