package exam.bus.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IExamQuestionService;
import exam.bus.IExamRecordService;
import exam.bus.IExamService;
import exam.bus.util.ExamUtil;
import exam.common.enums.*;
import exam.common.errorcode.CommonError;
import exam.common.errorcode.ExamError;
import exam.common.errorcode.StuExamError;
import exam.common.vo.*;
import exam.dao.entity.*;
import exam.dao.mapper.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssm.common.entity.BaseEntity;
import ssm.common.entity.ComboBox;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/18 10:53
 */
@Service
@AllArgsConstructor
@Slf4j
public class ExamServiceImpl extends ServiceImpl<ExamMapper, ExamEntity> implements IExamService {

    private IExamQuestionService examQuestionService;

    private QuestionCategoryMapper categoryMapper;

    private QuestionMapper questionMapper;

    private ExamStudentMapper examStudentMapper;

    private ExamRecordOptionMapper recordOptionMapper;

    private ExamRecordMapper examRecordMapper;

    private IExamRecordService examRecordService;

    private AnswerStatisticsMapper answerStatisticsMapper;


    @Transactional
    @Override
    public void add(ExamVO vo) {
        vo.setId(null);
        // 去除试卷名称后面的空格
        vo.setName(vo.getName().trim());
        this.valid(vo);

        // 获取请求中考试试卷主体信息
        final ExamEntity examEntity = new ExamEntity();
        BeanUtils.copyProperties(vo, examEntity);
        examEntity.setStatus(ExamStatus.UN_PUBLISHED);
        // 保存试卷主体信息
        this.save(examEntity);

        //保存试卷问题参数
        this.saveExamQuestion(examEntity.getId(), vo.getExamQuestions());
    }


    @Transactional
    @Override
    public void update(ExamVO vo) {
        CommonError.NOT_NULL.notNull(vo.getId(), "试卷id");
        this.valid(vo);

        // 获取请求中考试试卷主体信息
        ExamEntity examEntity = this.getById(vo.getId());
        ExamError.NOT_FIND_EXAM.notNull(examEntity);
        //未发布的试卷才可以修改
        ExamError.EXAM_PUBLISH_FAIL.isTrue(examEntity.getStatus() == ExamStatus.UN_PUBLISHED, examEntity.getStatus().getDesc());

        vo.setName(vo.getName().trim());
        BeanUtils.copyProperties(vo, examEntity);
        examEntity.setStatus(ExamStatus.UN_PUBLISHED);
        // 保存试卷主体信息
        this.updateById(examEntity);

        //删除试卷问题参数
        this.examQuestionService.remove(new LambdaQueryWrapper<ExamQuestionEntity>().eq(ExamQuestionEntity::getExamId, examEntity.getId()));
        //保存试卷问题参数
        this.saveExamQuestion(examEntity.getId(), vo.getExamQuestions());
    }

    private void valid(ExamVO vo) {
        //总分数必须大于等于通过分数
        ExamError.EXAM_SCORE_GE_PASS_SCORE.isTrue(NumberUtil.isGreaterOrEqual(vo.getScore(), vo.getPassScore()));
        //考试考试日期必须大于当前时间(允许5分钟误差)
        ExamError.EXAM_START_TIME_ERROR.isTrue(vo.getStartTime().isAfter(LocalDateTime.now().minusMinutes(5)));
        //考试截止日期必须大于考试开始日期
        ExamError.EXAM_END_TIME_ERROR.isTrue(vo.getEndTime().isAfter(vo.getStartTime()));
        //考试开始时间+考试持续时间必须小于考试结束时间
        ExamError.EXAM_TOTAL_TIME_ERROR.isTrue(vo.getStartTime().plusMinutes(vo.getLastTime()).isBefore(vo.getEndTime()));

        //题目不能为空
        ExamError.NOT_FIND_QUESTION.isTrue(CollUtil.isNotEmpty(vo.getExamQuestions()));


        //试卷名称唯一
        long count = this.count(new LambdaQueryWrapper<ExamEntity>().eq(ExamEntity::getName, vo.getName().trim()).ne(vo.getId() != null, BaseEntity::getId, vo.getId()));
        ExamError.EXAM_NAME_HAD_EXIST.isTrue(count < 1);

        //判断前端显示总分数与试题计算分数是否相等
        BigDecimal sum = vo.getExamQuestions().stream().map(eq -> eq.getScore().multiply(BigDecimal.valueOf(eq.getNum()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        ExamError.SUM_SCORE_ERROR.isTrue(NumberUtil.equals(vo.getScore(), sum));
    }

    /**
     * 保存试卷问题参数
     *
     * @param examId
     * @param list
     */
    private void saveExamQuestion(final Long examId, final List<ExamQuestionVO> list) {
        // 试卷中是否有简答题处理
        for (ExamQuestionVO vo : list) {
            if (vo.getType() == QuestionTypeEnum.ESSAY) {
                // 修改试卷是否需要人工阅卷状态处理
                this.update(new LambdaUpdateWrapper<ExamEntity>().set(ExamEntity::getAutoMarking, EnableEnum.DISABLE).eq(BaseEntity::getId, examId));
                break;
            }
        }
        //获取试题类别
        final Map<Long, String> categoryMap = this.getCategoryMap(list);
        //获取题目类型
        final Map<String, List<QuestionEntity>> questionMap = this.getQuestionMap(categoryMap.keySet());

        List<ExamQuestionEntity> entities = list.stream().map(eq -> this.convertExamQuestion(examId, eq, questionMap, categoryMap)).collect(Collectors.toList());
        this.examQuestionService.saveBatch(entities);
    }


    /**
     * 获取试题类别 map
     *
     * @param list
     * @return
     */
    private Map<Long, String> getCategoryMap(List<ExamQuestionVO> list) {
        Set<String> set = new HashSet<>();
        //试题类目id
        Set<Long> categoryIds = new HashSet<>();

        //判断类目不能重复
        list.stream().forEach(v -> {
            String key = ExamUtil.generateQuestionKey(v.getQuestionCategoryId(), v.getType(), v.getDifficulty());
            ExamError.EXAM_CATEGORY_REPETITION.isFalse(set.contains(key));
            set.add(key);
            categoryIds.add(v.getQuestionCategoryId());
        });


        List<QuestionCategoryEntity> categoryList = this.categoryMapper.selectList(new LambdaQueryWrapper<QuestionCategoryEntity>()
                .eq(QuestionCategoryEntity::getStatus, EnableEnum.ENABLE)
                .in(BaseEntity::getId, categoryIds));
        //判断类目id存在
        ExamError.CATEGORY_ERROR.isTrue(CollUtil.isNotEmpty(categoryList) && categoryList.size() == categoryIds.size());

        return categoryList.stream().collect(Collectors.toMap(BaseEntity::getId, QuestionCategoryEntity::getName));
    }

    /**
     * 获取问题map
     * <p>
     * key为 类目id-类型name()-难易程度name()
     * value 对应问题条数
     *
     * @param categoryIds
     * @return
     */
    private Map<String, List<QuestionEntity>> getQuestionMap(Set<Long> categoryIds) {
        List<QuestionEntity> list = this.questionMapper.selectList(new LambdaQueryWrapper<QuestionEntity>()
                .eq(QuestionEntity::getStatus, EnableEnum.ENABLE)
                .in(QuestionEntity::getQuestionCategoryId, categoryIds));
        return ExamUtil.getQuestionMap(list);
    }


    private ExamQuestionEntity convertExamQuestion(final Long examId, ExamQuestionVO vo, Map<String, List<QuestionEntity>> questionMap, Map<Long, String> categoryMap) {
        //题目数量必须大于0
        ExamError.QUESTION_NUM_ERROR.isTrue(vo.getNum() > 0);
        //题目分值必须大于0
        ExamError.QUESTION_SCORE_ERROR.isTrue(NumberUtil.isGreater(vo.getScore(), BigDecimal.ZERO));

        String key = ExamUtil.generateQuestionKey(vo.getQuestionCategoryId(), vo.getType(), vo.getDifficulty());
        //题目数量必须小于等于题库数量
        ExamError.QUESTION_NUM_TOO_LESS.isTrue(questionMap.containsKey(key) && questionMap.get(key).size() >= vo.getNum(), categoryMap.get(vo.getQuestionCategoryId()), vo.getType().getDesc(), vo.getDifficulty().getDesc());

        return new ExamQuestionEntity()
                .setExamId(examId)
                .setType(vo.getType())
                .setScore(vo.getScore())
                .setNum(vo.getNum())
                .setDifficulty(vo.getDifficulty())
                .setQuestionCategoryId(vo.getQuestionCategoryId());
    }


    @Override
    @Transactional
    public String publishExam(Long id) {
        ExamEntity examEntity = this.getById(id);
        ExamError.NOT_FIND_EXAM.notNull(examEntity);
        //未发布的试卷才可以发布
        ExamError.EXAM_PUBLISH_FAIL.notNull(examEntity.getStatus() == ExamStatus.UN_PUBLISHED, examEntity.getStatus().getDesc());

        //当前时间+考试持续时间必须小于考试结束时间
        ExamError.NOW_TIME_LE_END_TIME_ERROR.isTrue(LocalDateTime.now().plusMinutes(examEntity.getLastTime()).isBefore(examEntity.getEndTime()));

        // 发布的试卷必须要有考生
        Integer count =
                examStudentMapper.selectCount(new LambdaQueryWrapper<ExamStudentEntity>().eq(ExamStudentEntity::getExamId, id));
        ExamError.EXAM_PUBLISH_NO_STUDENT.isTrue(count > 0);

        // 修改试卷状态为已发布
        this.update(new LambdaUpdateWrapper<ExamEntity>().set(ExamEntity::getStatus, ExamStatus.PUBLISHED).eq(BaseEntity::getId, examEntity.getId()));
        return examEntity.getName();
    }


    @Override
    @Transactional
    public void delete(Set<Long> ids) {
        CommonError.NOT_NULL.notEmpty(ids, "试卷id");

        // 删除试卷关联的考生
        this.examStudentMapper.delete(new LambdaQueryWrapper<ExamStudentEntity>().in(ExamStudentEntity::getExamId, ids));

        //删除试卷题目
        this.examQuestionService.remove(new LambdaQueryWrapper<ExamQuestionEntity>().in(ExamQuestionEntity::getExamId, ids));

        //删除考试记录题目选项
        this.recordOptionMapper.delete(new LambdaQueryWrapper<ExamRecordOptionEntity>().in(ExamRecordOptionEntity::getExamId, ids));

        //删除考试记录试题
        this.examRecordMapper.delete(new LambdaQueryWrapper<ExamRecordEntity>().in(ExamRecordEntity::getExamId, ids));

        // 删除试卷
        this.removeByIds(ids);
    }

    @Override
    public ExamVO findExamInfo(Long id) {
        ExamEntity examEntity = getById(id);
        ExamError.NOT_FIND_EXAM.notNull(examEntity);
        ExamVO vo = new ExamVO();
        BeanUtil.copyProperties(examEntity, vo);
        // 校验试卷问题数量
        List<ExamQuestionEntity> list = this.examQuestionService.findExamQuestionByExamId(id);
        if (CollUtil.isEmpty(list)) {
            return vo;
        }
        List<ExamQuestionVO> questionList = list.stream().map(question -> {
            ExamQuestionVO questionVO = new ExamQuestionVO();
            BeanUtil.copyProperties(question, questionVO);
            return questionVO;
        }).collect(Collectors.toList());

        vo.setExamQuestions(questionList);
        return vo;
    }

    @Override
    public Page<ExamEntity> pageList(ExamQueryVO vo, Page page) {
        LambdaQueryWrapper<ExamEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(vo.getName() != null, ExamEntity::getName, vo.getName())
                .like(vo.getCreateBy() != null, exam.dao.entity.BaseEntity::getCreateBy, vo.getCreateBy())
                .eq(vo.getStatus() != null, ExamEntity::getStatus, vo.getStatus())
                .eq(vo.getScore() != null, ExamEntity::getScore, vo.getScore())
                .ge(vo.getStartTime() != null, ExamEntity::getEndTime, vo.getStartTime())
                .le(vo.getEndTime() != null, ExamEntity::getEndTime, vo.getEndTime());

        queryWrapper.orderByDesc(BaseEntity::getUpdateTime);
        return page(page, queryWrapper);
    }

    /**
     * 手动结束考试
     *
     * @param examId
     */
    @Override
    public String endExam(Long examId) {

        final ExamEntity exam = this.getById(examId);
        ExamError.NOT_FIND_EXAM.notNull(exam);
        ExamError.EXAM_STATUS_ERROR.isTrue(exam.getStatus() == ExamStatus.PUBLISHED, exam.getStatus().getDesc());
        //修改考生状态
        this.updateExamStatus(exam);

        //阅卷
        this.marking(examId);
        return exam.getName();
    }

    private void updateExamStatus(ExamEntity exam) {
        //修改待考状态的考生状态为缺考
        this.examStudentMapper.update(null, new LambdaUpdateWrapper<ExamStudentEntity>()
                .eq(ExamStudentEntity::getExamId, exam.getId())
                .eq(ExamStudentEntity::getStatus, ExamStuStatusEnum.TO_BE_EXAM)
                .set(ExamStudentEntity::getStatus, ExamStuStatusEnum.MISS_EXAM));

        //修改考试中的考生状态为已完成考试
        this.examStudentMapper.update(null, new LambdaUpdateWrapper<ExamStudentEntity>()
                .set(ExamStudentEntity::getStatus, ExamStuStatusEnum.COMPLETE)
                .set(ExamStudentEntity::getEndTime, exam.getEndTime())
                .eq(ExamStudentEntity::getExamId, exam.getId())
                .eq(ExamStudentEntity::getStatus, ExamStuStatusEnum.EXAM_ING));

        //修改试卷状态为阅卷中
        this.update(new LambdaUpdateWrapper<ExamEntity>().set(ExamEntity::getStatus, ExamStatus.MARKING).eq(BaseEntity::getId, exam.getId()));
    }

    /**
     * 自动阅卷
     *
     * @param examId 试卷id
     */
    @Override
    public void marking(Long examId) {
        final ExamEntity exam = this.getById(examId);
        ExamError.NOT_FIND_EXAM.notNull(exam);
        log.info("自动阅卷：试卷id：{}，试卷名称：{}", exam.getId(), exam.getName());
        ExamError.EXAM_STATUS_ERROR.isTrue(exam.getStatus() == ExamStatus.MARKING, exam.getStatus().getDesc());

        //获取考试记录
        List<ExamRecordEntity> list = this.examRecordMapper.selectList(new LambdaQueryWrapper<ExamRecordEntity>().eq(ExamRecordEntity::getExamId, exam.getId()));
        if (CollUtil.isEmpty(list)) {
            return;
        }
        // 查询是否存在简答题
        boolean containsEssay = list.stream().anyMatch(t -> t.getType() == QuestionTypeEnum.ESSAY);
        update(new LambdaUpdateWrapper<ExamEntity>().set(ExamEntity::getAutoMarking, EnableEnum.DISABLE).set(ExamEntity::getStatus, ExamStatus.END).eq(ExamEntity::getId, exam.getId()));

        //获取所有考生id
        Set<Long> stuIds = this.examStudentMapper.findStuIdByExamId(exam.getId());
        log.info("自动阅卷： 考生id集合：{}", stuIds);

        Map<Long, List<ExamRecordEntity>> collectByStudent = list.stream().collect(Collectors.groupingBy(ExamRecordEntity::getStuId));
        collectByStudent.forEach((k, v) -> {
            log.info("考生：{} 考试：{}，开始自动阅卷", k, examId);
            BigDecimal sumScore = finalScore(v);
            updateExamStuStatus(exam, k, sumScore, containsEssay);
        });
    }

    @Override
    public void artificialMarkingExam(ArtificialMarkingVO vo) {
        final ExamEntity exam = this.getById(vo.getExamId());
        ExamError.NOT_FIND_EXAM.notNull(exam);
        log.info("开始人工阅卷：试卷id：{}，试卷名称：{},试卷状态：{}，考生id：{}", exam.getId(), exam.getName(), exam.getStatus(), vo.getStuId());

        //获取考生试卷信息
        ExamStudentEntity examStudentEntity = examStudentMapper.selectOne(new LambdaQueryWrapper<ExamStudentEntity>().eq(ExamStudentEntity::getExamId, vo.getExamId()).eq(ExamStudentEntity::getStuId, vo.getStuId()));
        StuExamError.STU_EXAM_NOT_FIND.notNull(examStudentEntity);
        StuExamError.STU_EXAM_STATUS_ERROR.isTrue(examStudentEntity.getStatus() == ExamStuStatusEnum.COMPLETE || examStudentEntity.getStatus() == ExamStuStatusEnum.EXAM_MARKING, examStudentEntity.getStatus().getDesc());

        //获取考生的考试记录
        List<ExamRecordEntity> list = this.examRecordMapper.selectList(new LambdaQueryWrapper<ExamRecordEntity>().eq(ExamRecordEntity::getExamId, exam.getId()).eq(ExamRecordEntity::getStuId, vo.getStuId()));
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (ExamRecordEntity entity : list) {
            if (entity.getType() != QuestionTypeEnum.ESSAY) {
                //计算考生（选择，判断）试题得分
                if (CharSequenceUtil.isBlank(entity.getStuAnswer())) {
                    continue;
                }
                Set<String> answers = CollUtil.newHashSet(StrUtil.split(entity.getAnswer(), ","));
                Set<String> stuAnswers = CollUtil.newHashSet(StrUtil.split(entity.getStuAnswer(), ","));
                boolean correct = answers.size() == stuAnswers.size() && CollUtil.containsAll(answers, stuAnswers);
                if (correct) {
                    entity.setFinalScore(entity.getScore());
                    this.examRecordMapper.updateById(entity);
                }
            }
        }
        // 修改考生状态阅卷中
        examStudentMapper.update(null, new LambdaUpdateWrapper<ExamStudentEntity>()
                .set(ExamStudentEntity::getStatus, ExamStuStatusEnum.EXAM_MARKING)
                .eq(ExamStudentEntity::getExamId, vo.getExamId())
                .eq(ExamStudentEntity::getStuId, vo.getStuId()));

    }

    @Override
    public void commitMarkingExam(SubmitMarkingResult vo) {
        final ExamEntity exam = this.getById(vo.getExamId());
        ExamError.NOT_FIND_EXAM.notNull(exam);
        log.info("提交人工阅卷信息：试卷id：{}，试卷名称：{} ，考生id：{}", exam.getId(), exam.getName(), vo.getStuId());
        ExamError.EXAM_STATUS_ERROR.isTrue(exam.getStatus() == ExamStatus.PUBLISHED || exam.getStatus() == ExamStatus.MARKING || exam.getStatus() == ExamStatus.END, exam.getStatus().getDesc());

        // 更新考生试题所得分数
        List<SubmitMarkingScore> markingScoreList = vo.getMarkingScoreList();
        if (CollUtil.isNotEmpty(markingScoreList)) {
            markingScoreList.stream().forEach(score -> {
                ExamRecordEntity examRecordEntity = examRecordService.getById(score.getRecordId());
                ExamError.EXAM_QUESTION_NOT_EXIST.notNull(examRecordEntity);
                if (score.getFinalScore() == null) {
                    ExamError.EXAM_GET_SCORE_NOT_NULL.assertThrow(score.getRecordId());
                }
                if (score.getFinalScore().compareTo(examRecordEntity.getScore()) == 1) {
                    ExamError.EXAM_GET_SCORE_ERROR.assertThrow(score.getRecordId());
                }
                examRecordService.update(new LambdaUpdateWrapper<ExamRecordEntity>()
                        .set(ExamRecordEntity::getFinalScore, score.getFinalScore())
                        .eq(BaseEntity::getId, score.getRecordId())
                        .eq(ExamRecordEntity::getExamId, vo.getExamId())
                        .eq(ExamRecordEntity::getStuId, vo.getStuId()));
            });
        }
        // 统计考生试卷总分数
        BigDecimal finalScore = studentFinalScore(vo.getExamId(), vo.getStuId());
        //修改考试试卷状态
        this.endExamStuMarking(exam, vo.getStuId(), finalScore);

    }

    @Override
    public Page<ArtificialMarkExamResultVO> getMarkingExamList(ArtificialMarkQueryVO vo, Page page) {
        return this.getBaseMapper().getMarkingExamList(vo, page);
    }

    @Override
    public Page<ArtificialMarkExamResultVO> getStudentExamList(StuExamQueryVO vo, Page page) {
        return this.getBaseMapper().getStudentExamList(vo, page);
    }

    @Override
    public List<ComboBox> comboBoxStudentStatus() {
        return CollUtil.newArrayList(ExamStuStatusEnum.values()).stream().map(s -> {
            ComboBox box = new ComboBox();
            box.setLabel(s.getDesc());
            box.setValue(String.valueOf(s.getCode()));
            return box;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ComboBox> comboBoxExamStatus() {
        return CollUtil.newArrayList(ExamStatus.values()).stream().map(s -> {
            ComboBox box = new ComboBox();
            box.setLabel(s.getDesc());
            box.setValue(String.valueOf(s.getCode()));
            return box;
        }).collect(Collectors.toList());
    }

    @Override
    public void autoEndExamStatus(Long examId) {
        final ExamEntity exam = this.getById(examId);
        ExamError.NOT_FIND_EXAM.notNull(exam);
        log.info("自动结束试卷：试卷id：{}，试卷名称：{}", exam.getId(), exam.getName());

        //修改待考状态的考生状态为缺考
        this.examStudentMapper.update(null, new LambdaUpdateWrapper<ExamStudentEntity>()
                .eq(ExamStudentEntity::getExamId, exam.getId())
                .eq(ExamStudentEntity::getStatus, ExamStuStatusEnum.TO_BE_EXAM)
                .set(ExamStudentEntity::getStatus, ExamStuStatusEnum.MISS_EXAM));

        //修改考试中的考生状态为已完成考试
        this.examStudentMapper.update(null, new LambdaUpdateWrapper<ExamStudentEntity>()
                .set(ExamStudentEntity::getStatus, ExamStuStatusEnum.COMPLETE)
                .set(ExamStudentEntity::getEndTime, exam.getEndTime())
                .eq(ExamStudentEntity::getExamId, exam.getId())
                .eq(ExamStudentEntity::getStatus, ExamStuStatusEnum.EXAM_ING));

        //修改试卷状态为已结束
        this.update(new LambdaUpdateWrapper<ExamEntity>().set(ExamEntity::getStatus, ExamStatus.END).eq(BaseEntity::getId, exam.getId()));

        // 进行(选择、判断题)阅卷操作
        // 1.获取考试记录
        List<ExamRecordEntity> list = this.examRecordMapper.selectList(new LambdaQueryWrapper<ExamRecordEntity>().eq(ExamRecordEntity::getExamId, exam.getId()));
        if (CollUtil.isEmpty(list)) {
            return;
        }
        // 查询是否存在简答题
        boolean containsEssay = list.stream().anyMatch(t -> t.getType() == QuestionTypeEnum.ESSAY);
        // 2.获取所有考生id
        Set<Long> stuIds = this.examStudentMapper.findStuIdByExamId(exam.getId());
        log.info("自动结束试卷时进行自动阅卷： 考生id集合：{}", stuIds);
        Map<Long, List<ExamRecordEntity>> map = list.stream().collect(Collectors.toMap(ExamRecordEntity::getStuId, CollUtil::newArrayList, (oldList, newList) -> (List<ExamRecordEntity>) CollUtil.addAll(oldList, newList)));
        stuIds.stream().forEach(stuId -> {
            BigDecimal sumScore = NumberUtil.toBigDecimal(-1);
            if (map.containsKey(stuId)) {
                //计算考生（选择，判断）试题得分
                sumScore = this.finalScore(map.get(stuId));
            }
            //修改考试试卷状态
            this.updateExamStuStatus(exam, stuId, sumScore, containsEssay);
        });
    }

    @Override
    public List<ExamEntity> getNotEndExamList() {
        return baseMapper.selectList(new LambdaQueryWrapper<ExamEntity>());
    }

    @Override
    public List<RecentExamResultVO> recentExamStatistics() {
        List<RecentExamResultVO> voList = baseMapper.recentExamStatistics();
        voList.forEach(RecentExamResultVO::computePassed);
        return voList;
    }

    /**
     * 考生考试所得总分数统计
     *
     * @param examId
     * @param stuId
     * @return
     */
    private BigDecimal studentFinalScore(Long examId, Long stuId) {
        BigDecimal finalScore = BigDecimal.ZERO;
        List<ExamRecordEntity> examRecordEntities = examRecordMapper.selectList(new LambdaQueryWrapper<ExamRecordEntity>().eq(ExamRecordEntity::getExamId, examId).eq(ExamRecordEntity::getStuId, stuId));
        if (CollUtil.isNotEmpty(examRecordEntities)) {
            for (ExamRecordEntity entity : examRecordEntities) {
                finalScore = finalScore.add(entity.getFinalScore());
            }
        }
        return finalScore;
    }

    /**
     * 计算客观（选择，判断）试题得分
     *
     * @param list
     * @return
     */
    private BigDecimal finalScore(List<ExamRecordEntity> list) {
        BigDecimal score = BigDecimal.ZERO;
        for (ExamRecordEntity entity : list) {
            if (CharSequenceUtil.isBlank(entity.getStuAnswer()) || QuestionTypeEnum.ESSAY == entity.getType()) {
                continue;
            }
            Set<String> answers = CollUtil.newHashSet(StrUtil.split(entity.getAnswer(), ","));
            Set<String> stuAnswers = CollUtil.newHashSet(StrUtil.split(entity.getStuAnswer(), ","));
            boolean correct = answers.size() == stuAnswers.size() && CollUtil.containsAll(answers, stuAnswers);
            if (correct) {
                entity.setFinalScore(entity.getScore());
                this.examRecordMapper.updateById(entity);
                score = score.add(entity.getFinalScore());
            }
            statisticalFailed(entity, correct);
        }
        return score;
    }

    /**
     * 统计题目正确率
     */
    private void statisticalFailed(ExamRecordEntity entity, boolean correct) {
        if (Objects.isNull(entity.getQuestionId())) {
            return;
        }
        Integer qustionContains = answerStatisticsMapper.selectByQustionId(entity.getQuestionId());
        Integer total = 1;
        Integer fail = correct ? 0 : 1;
        if (Objects.nonNull(qustionContains)) {
            answerStatisticsMapper.updateTotalAndFailByQustionId(entity.getQuestionId(), fail, total);
        } else {
            AnswerStatisticsEntity answerStatisticsEntity = new AnswerStatisticsEntity()
                    .setQuestionId(entity.getQuestionId())
                    .setTotalCount(total)
                    .setErrorCount(fail);
            answerStatisticsMapper.insert(answerStatisticsEntity);
        }
    }

    /**
     * 修改考试状态
     *
     * @param exam
     * @param stuId
     * @param sumScore
     */
    private void updateExamStuStatus(ExamEntity exam, Long stuId, BigDecimal sumScore, boolean containsEssay) {
        if (NumberUtil.equals(sumScore, NumberUtil.toBigDecimal(-1))) {
            return;
        }
        // 存在简单题时要求必须阅卷
        ExamStuStatusEnum status;
        if (containsEssay) {
            status = ExamStuStatusEnum.EXAM_MARKING;
        } else {
            if (NumberUtil.isGreaterOrEqual(sumScore, exam.getPassScore())) {
                status = ExamStuStatusEnum.PASS;
            } else {
                status = ExamStuStatusEnum.NOT_PASS;
            }
        }

        log.info("阅卷结果：试卷id：{},试卷名称:{},考试通过分数:{},考生id:{},考生总得分:{},考试结果:{}", exam.getId(), exam.getName(), exam.getPassScore(), stuId, sumScore, status);
        this.examStudentMapper.update(null, new LambdaUpdateWrapper<ExamStudentEntity>()
                .set(ExamStudentEntity::getStatus, status)
                .set(ExamStudentEntity::getTotalScore, sumScore)
                .eq(ExamStudentEntity::getStuId, stuId)
                .eq(ExamStudentEntity::getExamId, exam.getId()));
    }

    /**
     * 完成考生试卷阅卷
     *
     * @param exam
     * @param stuId
     * @param sumScore
     */
    private void endExamStuMarking(ExamEntity exam, Long stuId, BigDecimal sumScore) {
        if (NumberUtil.equals(sumScore, NumberUtil.toBigDecimal(-1))) {
            return;
        }
        ExamStuStatusEnum status = ExamStuStatusEnum.NOT_PASS;
        if (NumberUtil.isGreaterOrEqual(sumScore, exam.getPassScore())) {
            status = ExamStuStatusEnum.PASS;
        }

        log.info("完成考生试卷阅卷结果：试卷id：{},试卷名称:{},考试通过分数:{},考生id:{},考生总得分:{},考试结果:{}", exam.getId(), exam.getName(), exam.getPassScore(), stuId, sumScore, status);
        this.examStudentMapper.update(null, new LambdaUpdateWrapper<ExamStudentEntity>()
                .set(ExamStudentEntity::getStatus, status)
                .set(ExamStudentEntity::getTotalScore, sumScore)
                .eq(ExamStudentEntity::getStuId, stuId)
                .eq(ExamStudentEntity::getExamId, exam.getId()));
    }

}
