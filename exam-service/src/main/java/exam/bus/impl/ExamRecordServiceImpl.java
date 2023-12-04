package exam.bus.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IExamRecordService;
import exam.bus.util.ExamOptionVO;
import exam.bus.util.ExamUtil;
import exam.bus.util.StuExamRecordQuestionVO;
import exam.common.enums.EnableEnum;
import exam.common.enums.QuestionTypeEnum;
import exam.common.errorcode.ExamError;
import exam.common.errorcode.StuExamError;
import exam.dao.entity.*;
import exam.dao.mapper.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssm.common.util.AuthInfoUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordMapper, ExamRecordEntity> implements IExamRecordService {

    private ExamStudentMapper examStudentMapper;

    private ExamQuestionMapper examQuestionMapper;

    private QuestionCategoryMapper categoryMapper;

    private QuestionMapper questionMapper;

    private QuestionOptionMapper questionOptionMapper;

    private ExamRecordOptionMapper examRecordOptionMapper;

    /**
     * 生成指定试卷的所有考生考题
     *
     * @param examId 试卷id
     */
    @Override
    @Transactional
    public void generateStuExam(final Long examId) {
        //考生试卷验证
        LambdaQueryWrapper<ExamStudentEntity> wrapper = new LambdaQueryWrapper<ExamStudentEntity>()
                .eq(ExamStudentEntity::getExamId, examId);
        List<ExamStudentEntity> stuList = this.examStudentMapper.selectList(wrapper);
        //验证是否已经选择考生
        ExamError.NOT_SELECTED_STU.notEmpty(stuList);

        //试卷问题参数
        final List<ExamQuestionEntity> list = this.examQuestionMapper.selectList(new LambdaQueryWrapper<ExamQuestionEntity>()
                .eq(ExamQuestionEntity::getExamId, examId));

        Set<Long> categoryIds = list.stream().map(ExamQuestionEntity::getQuestionCategoryId).collect(Collectors.toSet());
        //试卷类别集合
        final Map<Long, String> categoryMap = this.getCategoryMap(categoryIds);
        //问题集合
        final Map<String, List<QuestionEntity>> questionMap = this.getQuestionMap(categoryIds);
        //问题选项集合
        final Map<Long, List<QuestionOptionEntity>> optionMap = this.getQuestionOption(categoryIds);

        stuList.stream().forEach(stu -> this.generateStuExam(stu, list, categoryMap, questionMap, optionMap));

    }


    /**
     * 生成指定考生试卷
     *
     * @param examId 试卷id
     * @param userId 考试id
     */
    @Override
    @Transactional
    public void generateStuExam(Long examId, Long userId) {
        //考生试卷验证
        LambdaQueryWrapper<ExamStudentEntity> wrapper = new LambdaQueryWrapper<ExamStudentEntity>()
                .eq(ExamStudentEntity::getExamId, examId)
                .eq(ExamStudentEntity::getStuId, userId);
        ExamStudentEntity examStudent = this.examStudentMapper.selectOne(wrapper);
        StuExamError.STU_INFO_ERROR.notNull(examStudent);

        //试卷问题参数
        final List<ExamQuestionEntity> list = this.examQuestionMapper.selectList(new LambdaQueryWrapper<ExamQuestionEntity>()
                .eq(ExamQuestionEntity::getExamId, examId));

        Set<Long> categoryIds = list.stream().map(ExamQuestionEntity::getQuestionCategoryId).collect(Collectors.toSet());
        //试卷类别集合
        final Map<Long, String> categoryMap = this.getCategoryMap(categoryIds);
        //问题集合
        final Map<String, List<QuestionEntity>> questionMap = this.getQuestionMap(categoryIds);
        //问题选项集合
        final Map<Long, List<QuestionOptionEntity>> optionMap = this.getQuestionOption(categoryIds);

        //生成考试试卷
        this.generateStuExam(examStudent, list, categoryMap, questionMap, optionMap);

    }

    private void generateStuExam(ExamStudentEntity examStudent, final List<ExamQuestionEntity> list, final Map<Long, String> categoryMap,
                                 final Map<String, List<QuestionEntity>> questionMap, final Map<Long, List<QuestionOptionEntity>> optionMap) {

        StuExamRecordQuestionVO stuExam = new StuExamRecordQuestionVO();
        //生成考生试卷
        this.generateStuExam(examStudent, list, categoryMap, questionMap, optionMap, stuExam);

        //保存考生问题信息
        if (CollUtil.isNotEmpty(stuExam.getRecordList())) {
            this.baseMapper.batchSave(stuExam.getRecordList());
        }
        //保存考生问题选项信息
        if (CollUtil.isNotEmpty(stuExam.getOptionList())) {
            this.examRecordOptionMapper.batchSave(stuExam.getOptionList());
        }
    }

    private Map<Long, String> getCategoryMap(Set<Long> categoryIds) {
        List<QuestionCategoryEntity> categoryList = this.categoryMapper.selectList(new LambdaQueryWrapper<QuestionCategoryEntity>().in(BaseEntity::getId, categoryIds));
        //判断类目
        ExamError.CATEGORY_ERROR.isTrue(CollUtil.isNotEmpty(categoryList) && categoryList.size() == categoryIds.size());
        return categoryList.stream().collect(Collectors.toMap(BaseEntity::getId, QuestionCategoryEntity::getName));
    }

    /**
     * 根据试题类别id获取问题选项集合
     * key 问题id
     * value 问题选项集合
     *
     * @param categoryIds
     * @return
     */
    private Map<Long, List<QuestionOptionEntity>> getQuestionOption(Set<Long> categoryIds) {
        List<QuestionOptionEntity> list = this.questionOptionMapper.findByCategoryIds(categoryIds);
        if (CollUtil.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream().collect(Collectors.toMap(QuestionOptionEntity::getQuestionId, ListUtil::toList, (o, n) -> (List<QuestionOptionEntity>) CollUtil.addAll(o, n)));
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

    private void generateStuExam(ExamStudentEntity stu, List<ExamQuestionEntity> list, Map<Long, String> categoryMap,
                                 Map<String, List<QuestionEntity>> questionMap, Map<Long, List<QuestionOptionEntity>> optionMap
            , StuExamRecordQuestionVO stuExam) {
        log.info("开始生成考生:{}的试卷，试卷id：{}", stu.getStuId(), stu.getExamId());

        //随机生成考生试卷
        list.stream().forEach(entity -> this.randomQuestion(entity, stu.getStuId(), questionMap, categoryMap, optionMap, stuExam));

        log.info("考生{}试卷生成结束", stu.getStuId());
    }

    private void randomQuestion(ExamQuestionEntity entity, Long userId, Map<String, List<QuestionEntity>> questionMap,
                                Map<Long, String> categoryMap, Map<Long, List<QuestionOptionEntity>> optionMap
            , StuExamRecordQuestionVO stuExam) {
        if (entity.getNum() == null || entity.getNum() == 0) {
            return;
        }

        String key = ExamUtil.generateQuestionKey(entity.getQuestionCategoryId(), entity.getType(), entity.getDifficulty());
        List<QuestionEntity> list = CollUtil.newArrayList(questionMap.get(key));

        //题库数量必须大于等于题目数量
        ExamError.QUESTION_NUM_TOO_LESS.isTrue(CollUtil.isNotEmpty(list) && list.size() >= entity.getNum(),
                categoryMap.get(entity.getQuestionCategoryId()), entity.getType().getDesc(), entity.getDifficulty().getDesc());


        int num = entity.getNum();

        while (num > 0) {
            int index = RandomUtil.randomInt(list.size());
            QuestionEntity question = list.get(index);

            log.info("试卷id：{},考生id：{}，问题：{}", entity.getExamId(), userId, question);

            //考试试题
            ExamRecordEntity recordEntity = this.convertRecordEntity(userId, question, entity);

            //单选题、多选需要复制选项
            if (question.getType() == QuestionTypeEnum.MULTIPLE || question.getType() == QuestionTypeEnum.SINGLE) {
                //随机生成试题选项记录
                ExamOptionVO examOption = this.randomQuestionOption(optionMap.get(question.getId()), recordEntity,question.getAnswer());
                recordEntity.setAnswer(examOption.getAnswer());
                stuExam.getOptionList().addAll(examOption.getList());
            }
            stuExam.getRecordList().add(recordEntity);
            list.remove(index);
            num--;
        }

    }

    private ExamOptionVO randomQuestionOption(List<QuestionOptionEntity> questionOptionList, ExamRecordEntity recordEntity,String serialNo) {
        ExamError.QUESTION_OPTION_ERROR.notEmpty(questionOptionList);
        return ExamUtil.getExamOption(questionOptionList, recordEntity, serialNo);
    }

    private ExamRecordEntity convertRecordEntity(Long userId, QuestionEntity question, ExamQuestionEntity entity) {
        ExamRecordEntity recordEntity = new ExamRecordEntity()
                .setQuestionId(question.getId())
                .setExamId(entity.getExamId())
                .setStuId(userId)
                .setType(question.getType())
                .setTitle(question.getTitle())
                .setAnswer(question.getAnswer())
                .setDifficulty(question.getDifficulty())
                .setAnalyse(question.getAnalyse())
                .setScore(entity.getScore())
                .setFinalScore(BigDecimal.ZERO);
        return (ExamRecordEntity) this.convertBaseEntity(recordEntity);
    }

    private BaseEntity convertBaseEntity(BaseEntity entity) {
        return (BaseEntity) entity.setUpdateBy(AuthInfoUtil.getUserName())
                .setCreateBy(AuthInfoUtil.getUserName())
                .setId(IdWorker.getId())
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
    }
}
