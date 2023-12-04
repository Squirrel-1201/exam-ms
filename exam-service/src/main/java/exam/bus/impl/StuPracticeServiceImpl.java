package exam.bus.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IPracticeRecordService;
import exam.bus.IStuPracticeService;
import exam.common.enums.PracticePaperStatus;
import exam.common.enums.PracticeStatus;
import exam.common.enums.QuestionTypeEnum;
import exam.common.enums.YNEnum;
import exam.common.errorcode.PracticePaperError;
import exam.common.vo.FinishPracticePaperVO;
import exam.common.vo.PracticeAnswerVO;
import exam.common.vo.PracticePaperDetailVO;
import exam.common.vo.PracticeQuestionDetailVO;
import exam.common.vo.QuestionOptionDetailVO;
import exam.common.vo.StudentPracticeVO;
import exam.common.vo.SubmitPracticePaperVO;
import exam.dao.entity.PracticeEntity;
import exam.dao.entity.PracticePaperEntity;
import exam.dao.entity.PracticeRecordEntity;
import exam.dao.entity.PracticeRecordOptionEntity;
import exam.dao.entity.PracticeStudentEntity;
import exam.dao.entity.UserEntity;
import exam.dao.mapper.PracticeMapper;
import exam.dao.mapper.PracticePaperMapper;
import exam.dao.mapper.PracticeRecordOptionMapper;
import exam.dao.mapper.PracticeStudentMapper;
import exam.dao.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0.0
 * @date: 2022/7/15 14:19
 * @author: yangbo
 */
@Service
@Slf4j
@AllArgsConstructor
public class StuPracticeServiceImpl implements IStuPracticeService {

    private PracticeMapper practiceMapper;

    private PracticeStudentMapper practiceStudentMapper;

    private PracticePaperMapper practicePaperMapper;

    private PracticeRecordOptionMapper practiceRecordOptionMapper;

    private IPracticeRecordService practiceRecordService;

    private UserMapper userMapper;

    @Override
    public IPage<StudentPracticeVO> practiceList(StudentPracticeVO vo, Page page) {
        IPage<StudentPracticeVO> voPage = practiceStudentMapper.selectPracticeList(vo, page);
        voPage.getRecords().forEach(StudentPracticeVO::changePracticeStudentStatus);
        return voPage;
    }

    @Override
    @Transactional
    public PracticePaperDetailVO startPractice(Long practiceId, Long userId) {
        //region 参数校检
        LambdaQueryWrapper<PracticePaperEntity> wrapper = new LambdaQueryWrapper<PracticePaperEntity>()
                .eq(PracticePaperEntity::getPracticeId, practiceId)
                .eq(PracticePaperEntity::getStuId, userId)
                .eq(PracticePaperEntity::getStatus, PracticePaperStatus.PRACTICE_ING);
        PracticePaperEntity paperEntity = practicePaperMapper.selectOne(wrapper);
        PracticePaperError.PRACTICE_CONTINUE.isNull(paperEntity);
        PracticeEntity practiceEntity = practiceMapper.selectById(practiceId);
        PracticePaperError.PRACTICE_NOT_FIND.notNull(practiceEntity);
        PracticePaperError.PRACTICE_STATUS_ERROR.isTrue(practiceEntity.getStatus() == PracticeStatus.PUBLISH);
        PracticePaperError.PRACTICE_OVER_TIME.isTrue(practiceEntity.getEndTime().isAfter(LocalDateTime.now().minusMinutes(practiceEntity.getLastTime())));
        PracticeStudentEntity studentEntity = practiceStudentMapper.selectOne(new LambdaQueryWrapper<PracticeStudentEntity>()
                .eq(PracticeStudentEntity::getPracticeId, practiceId)
                .eq(PracticeStudentEntity::getStuId, userId));
        PracticePaperError.PRACTICE_NOT_ALLOW_STUDENT.notNull(studentEntity);
        PracticePaperError.PRACTICE_OVER_TIMES.isTrue(practiceEntity.getTimes() > studentEntity.getUsedTimes());
        //endregion


        PracticePaperEntity paper = new PracticePaperEntity()
                .setPracticeId(practiceId)
                .setPracticeStuId(studentEntity.getId())
                .setStuId(userId)
                .setStartTime(LocalDateTime.now())
                .setEndTime(LocalDateTime.now().plusMinutes(practiceEntity.getLastTime()))
                .setStatus(PracticePaperStatus.PRACTICE_ING);

        practicePaperMapper.insert(paper);
        practiceStudentMapper.updateById(studentEntity.setUsedTimes(studentEntity.getUsedTimes() + 1));
        // 生成试卷
        practiceRecordService.generateStuPractice(practiceId, paper.getId());

        return getPracticePaperResult(practiceId, paper.getId());

    }

    @Override
    public PracticePaperDetailVO continuePractice(Long practiceId, Long userId) {
        PracticePaperEntity paper = practicePaperMapper.selectOne(new LambdaQueryWrapper<PracticePaperEntity>()
                .eq(PracticePaperEntity::getPracticeId, practiceId)
                .eq(PracticePaperEntity::getStuId, userId)
                .eq(PracticePaperEntity::getStatus, PracticePaperStatus.PRACTICE_ING));
        PracticePaperError.PRACTICE_NOT_FIND.notNull(paper);
        PracticeEntity practice = practiceMapper.selectById(practiceId);
        PracticePaperError.PRACTICE_NOT_FIND.notNull(practice);
        PracticePaperError.PRACTICE_STATUS_ERROR.isTrue(paper.getStartTime().minusMinutes(practice.getLastTime()).isBefore(LocalDateTime.now()));


        return getPracticePaperResult(practiceId, paper.getId());
    }

    @Override
    public PracticePaperDetailVO practicePaperResult(Long paperId) {
        PracticePaperEntity paper = practicePaperMapper.selectById(paperId);
        PracticePaperError.PAPER_NOT_FIND.notNull(paper);
        return getPracticePaperResult(paper.getPracticeId(), paperId);
    }

    @Override
    @Transactional
    public void submitAnswer(PracticeAnswerVO vo) {
        PracticePaperEntity paper = practicePaperMapper.selectById(vo.getPracticePaperId());
        PracticePaperError.PRACTICE_PSTATUS_ERROR.isTrue(paper.getStatus() == PracticePaperStatus.PRACTICE_ING);
        PracticeEntity practice = practiceMapper.selectById(paper.getPracticeId());
        PracticePaperError.PRACTICE_NOT_FIND.notNull(practice);
        PracticePaperError.PRACTICE_STATUS_ERROR.isTrue(paper.getStartTime().minusMinutes(practice.getLastTime()).isBefore(LocalDateTime.now()));
        PracticeRecordEntity result = practiceRecordService.getById(vo.getRecordId());
        result.setStuAnswer(vo.getAnswer());
        practiceRecordService.updateById(result);
    }

    @Override
    @Transactional
    public String submitPracticePaper(SubmitPracticePaperVO vo) {
        PracticePaperEntity paper = practicePaperMapper.selectById(vo.getPracticePaperId());
        PracticePaperError.PRACTICE_NOT_FIND.notNull(paper);
        PracticePaperError.PRACTICE_PSTATUS_ERROR.isTrue(paper.getStatus() == PracticePaperStatus.PRACTICE_ING);

        if (!vo.isConfirm()) {
            int count = practiceRecordService.getBaseMapper().selectCount(new LambdaQueryWrapper<PracticeRecordEntity>()
                    .eq(PracticeRecordEntity::getPracticePaperId, vo.getPracticePaperId())
                    .isNull(PracticeRecordEntity::getStuAnswer));
            PracticePaperError.PRACTICE_NOT_ALL_COMPLETED.isTrue(count <= 0, count);
        }

        paper.setStatus(PracticePaperStatus.PRACTICE_FINISH)
                .setEndTime(LocalDateTime.now());
        practicePaperMapper.updateById(paper);
        return practiceMapper.selectById(paper.getPracticeId()).getName();
    }

    @Override
    public IPage<FinishPracticePaperVO> queryFinishPaperList(FinishPracticePaperVO vo, Page page) {
        return practicePaperMapper.queryFinishPracticeList(vo, page);
    }

    @Override
    @Transactional
    public void autoEndPractice() {
        log.debug("开始检查已到达练习时间的练习");
        List<PracticeEntity> list = practiceMapper.selectList(new LambdaQueryWrapper<PracticeEntity>()
                .le(PracticeEntity::getEndTime, LocalDateTime.now())
                .ne(PracticeEntity::getStatus, PracticeStatus.END));
        if (CollUtil.isEmpty(list)) {
            log.debug("没有需要自动结束的练习........");
            return;
        }
        list.forEach(t -> endPractice(t.getId()));
        log.debug("自动结束练习已完成");
    }

    @Override
    public void autoMarking() {
        log.debug("开始自动阅卷");
        practicePaperMapper.update(null, new LambdaUpdateWrapper<PracticePaperEntity>()
                .le(PracticePaperEntity::getEndTime, LocalDateTime.now())
                .eq(PracticePaperEntity::getStatus, PracticePaperStatus.PRACTICE_ING)
                .set(PracticePaperEntity::getStatus, PracticePaperStatus.PRACTICE_FINISH));
        List<PracticePaperEntity> list = practicePaperMapper.selectList(new LambdaQueryWrapper<PracticePaperEntity>().eq(PracticePaperEntity::getStatus, PracticePaperStatus.PRACTICE_FINISH));
        if (CollUtil.isEmpty(list)) {
            log.debug("没有需要自动阅卷的练习试卷........");
            return;
        }
        list.forEach(this::marking);
    }

    private void marking(PracticePaperEntity paper) {
        PracticeEntity practice = practiceMapper.selectById(paper.getPracticeId());
        if (Objects.isNull(practice)) {
            log.info("练习不存在:{}", paper.getPracticeId());
            // 练习状态未更改
            return;
        }
        List<PracticeRecordEntity> recordList = practiceRecordService.getBaseMapper()
                .selectList(new LambdaQueryWrapper<PracticeRecordEntity>().eq(PracticeRecordEntity::getPracticePaperId, paper.getId()));
        log.info("练习试卷：{} 开始自动阅卷", recordList.get(0).getPracticePaperId());
        BigDecimal stuScore = computeStuScore(recordList);
        if (stuScore.compareTo(practice.getPassScore()) >= 0) {
            paper.setStatus(PracticePaperStatus.PASS);
        } else {
            paper.setStatus(PracticePaperStatus.NOT_PASS);
        }
        paper.setTotalScore(stuScore);
        practicePaperMapper.updateById(paper);
    }

    private BigDecimal computeStuScore(List<PracticeRecordEntity> recordList) {
        BigDecimal result = BigDecimal.ZERO;
        for (PracticeRecordEntity practiceRecord : recordList) {
            if (StringUtils.isEmpty(practiceRecord.getStuAnswer()) || practiceRecord.getType() == QuestionTypeEnum.ESSAY) {
                continue;
            }
            Set<String> answers = CollUtil.newHashSet(StrUtil.split(practiceRecord.getAnswer(), ","));
            Set<String> stuAnswers = CollUtil.newHashSet(StrUtil.split(practiceRecord.getStuAnswer(), ","));
            boolean correct = answers.size() == stuAnswers.size() && CollUtil.containsAll(answers, stuAnswers);
            if (correct) {
                practiceRecord.setFinalScore(practiceRecord.getScore());
                practiceRecordService.updateById(practiceRecord);
                result = result.add(practiceRecord.getFinalScore());
            }
        }

        return result;
    }


    private void endPractice(Long practiceId) {
        PracticeEntity practice = practiceMapper.selectById(practiceId);
        if (Objects.isNull(practice)) {
            return;
        }
        // 修改练习下相关试卷状态为正在练习中的改为已完成练习
        practicePaperMapper.update(null, new LambdaUpdateWrapper<PracticePaperEntity>()
                .eq(PracticePaperEntity::getPracticeId, practiceId)
                .eq(PracticePaperEntity::getStatus, PracticePaperStatus.PRACTICE_ING)
                .set(PracticePaperEntity::getStatus, PracticePaperStatus.PRACTICE_FINISH)
                .set(PracticePaperEntity::getEndTime, LocalDateTime.now()));

        practiceMapper.updateById(practice.setStatus(PracticeStatus.END));
    }

    private PracticePaperDetailVO getPracticePaperResult(Long practiceId, Long practicePaperId) {
        PracticePaperDetailVO result = new PracticePaperDetailVO();
        getPracticePaper(practiceId, practicePaperId, result);
        getPracticeQustion(practicePaperId, result);
        return result;
    }

    private void getPracticeQustion(Long practicePaperId, PracticePaperDetailVO result) {
        List<PracticeRecordEntity> list = practiceRecordService
                .getBaseMapper()
                .selectList(new LambdaQueryWrapper<PracticeRecordEntity>().eq(PracticeRecordEntity::getPracticePaperId, practicePaperId));
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<PracticeRecordOptionEntity> optionList = practiceRecordOptionMapper.selectList(new LambdaQueryWrapper<PracticeRecordOptionEntity>().eq(PracticeRecordOptionEntity::getPracticePaperId, practicePaperId).orderByDesc(PracticeRecordOptionEntity::getId));
        if (Objects.isNull(optionList)) {
            optionList = new ArrayList<>();
        }
        Map<Long, List<PracticeRecordOptionEntity>> collect = optionList.stream().collect(Collectors.groupingBy(PracticeRecordOptionEntity::getPracticeRecordId));
        list.forEach(t -> {
            PracticeQuestionDetailVO vo = new PracticeQuestionDetailVO();
            vo.setType(t.getType())
                    .setTitle(t.getTitle())
                    .setScore(t.getScore())
                    .setStuAnswer(t.getStuAnswer())
                    .setRecordId(t.getId());
            if (collect.get(t.getId()) != null) {
                vo.setOptions(collect.get(t.getId()).stream().map(option -> new QuestionOptionDetailVO(option.getSerialNo(), option.getTitle())).collect(Collectors.toList()));
            }
            if (result.getPaperStatus() == PracticePaperStatus.PASS || result.getPaperStatus() == PracticePaperStatus.NOT_PASS) {
                vo.setStuScore(t.getFinalScore())
                        .setStatus(NumberUtil.isGreater(t.getFinalScore(), BigDecimal.ZERO) ? YNEnum.YES : YNEnum.NO)
                        .setAnswer(t.getAnswer())
                        .setAnalyse(t.getAnalyse());
            }
            result.getList().add(vo);
        });
    }

    private void getPracticePaper(Long practiceId, Long practicePaperId, PracticePaperDetailVO result) {
        PracticeEntity practice = practiceMapper.selectById(practiceId);
        PracticePaperError.PRACTICE_NOT_FIND.notNull(practice);

        PracticePaperEntity paper = practicePaperMapper.selectById(practicePaperId);
        PracticePaperError.PAPER_NOT_FIND.notNull(paper);

        UserEntity student = userMapper.selectById(paper.getStuId());
        PracticePaperError.STUDENT_NOT_FIND.notNull(student);

        result.setUsername(student.getUsername())
                .setRealName(student.getRealName())
                .setPracticePaperId(practicePaperId)
                .setPaperStatus(paper.getStatus())
                .setPracticeName(practice.getName())
                .setPracticeScore(practice.getScore());

        if (result.getPaperStatus() == PracticePaperStatus.PASS || result.getPaperStatus() == PracticePaperStatus.NOT_PASS) {
            result.setStuScore(paper.getTotalScore());
        }
        if (result.getPaperStatus() == PracticePaperStatus.PRACTICE_ING) {
            LocalDateTime now = LocalDateTimeUtil.now();
            long remainingSecond = (practice.getLastTime() * 60) - LocalDateTimeUtil.between(paper.getStartTime(), now).getSeconds();
            long endTime = LocalDateTimeUtil.between(now, practice.getEndTime()).getSeconds();
            long minRemain = Math.min(remainingSecond, endTime);
            result.setRemainingSecond(minRemain < 0 ? 0L : minRemain);
        }

    }
}
