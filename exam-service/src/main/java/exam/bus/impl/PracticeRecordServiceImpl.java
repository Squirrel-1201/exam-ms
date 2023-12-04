package exam.bus.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IPracticeRecordOptionService;
import exam.bus.IPracticeRecordService;
import exam.bus.util.ExamUtil;
import exam.bus.util.PracticeOptionVO;
import exam.bus.util.PracticePaperRecordVO;
import exam.common.enums.EnableEnum;
import exam.common.enums.QuestionTypeEnum;
import exam.common.errorcode.ExamError;
import exam.dao.entity.BaseEntity;
import exam.dao.entity.PracticePaperEntity;
import exam.dao.entity.PracticeQuestionEntity;
import exam.dao.entity.PracticeRecordEntity;
import exam.dao.entity.QuestionCategoryEntity;
import exam.dao.entity.QuestionEntity;
import exam.dao.entity.QuestionOptionEntity;
import exam.dao.mapper.PracticePaperMapper;
import exam.dao.mapper.PracticeQuestionMapper;
import exam.dao.mapper.PracticeRecordMapper;
import exam.dao.mapper.QuestionCategoryMapper;
import exam.dao.mapper.QuestionMapper;
import exam.dao.mapper.QuestionOptionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @version 1.0.0
 * @date: 2022/7/15 17:50
 * @author: yangbo
 */
@AllArgsConstructor
@Slf4j
@Service
public class PracticeRecordServiceImpl extends ServiceImpl<PracticeRecordMapper, PracticeRecordEntity> implements IPracticeRecordService {

    private PracticePaperMapper practicePaperMapper;

    private PracticeQuestionMapper practiceQuestionMapper;

    private QuestionCategoryMapper categoryMapper;

    private QuestionMapper questionMapper;

    private QuestionOptionMapper questionOptionMapper;

    private IPracticeRecordOptionService practiceRecordOptionService;

    @Override
    public void generateStuPractice(Long practiceId, Long practicePaperId) {
        PracticePaperEntity paper = practicePaperMapper.selectById(practicePaperId);

        List<PracticeQuestionEntity> questionList = practiceQuestionMapper.selectList(new LambdaQueryWrapper<PracticeQuestionEntity>().eq(PracticeQuestionEntity::getPracticeId, practiceId));
        Set<Long> categoryIdSet = questionList.stream().map(PracticeQuestionEntity::getQuestionCategoryId).collect(Collectors.toSet());

        List<QuestionCategoryEntity> categoryList = this.categoryMapper.selectList(new LambdaQueryWrapper<QuestionCategoryEntity>().in(BaseEntity::getId, categoryIdSet));
        Map<Long, String> categoryMap = categoryList.stream().collect(Collectors.toMap(QuestionCategoryEntity::getId, QuestionCategoryEntity::getName));
        List<QuestionEntity> questionEntityList = questionMapper.selectList(new LambdaQueryWrapper<QuestionEntity>()
                .eq(QuestionEntity::getStatus, EnableEnum.ENABLE)
                .in(QuestionEntity::getQuestionCategoryId, categoryIdSet));
        Map<String, List<QuestionEntity>> questionMap = ExamUtil.getQuestionMap(questionEntityList);
        List<QuestionOptionEntity> optionList = questionOptionMapper.findByCategoryIds(categoryIdSet);
        Map<Long, List<QuestionOptionEntity>> optionsMap = optionList.stream().collect(Collectors.groupingBy(QuestionOptionEntity::getQuestionId));

        generatePracticePaper(paper, questionList, categoryMap, questionMap, optionsMap);

    }

    private void generatePracticePaper(PracticePaperEntity paper, List<PracticeQuestionEntity> questionList, Map<Long, String> categoryMap, Map<String, List<QuestionEntity>> questionMap, Map<Long, List<QuestionOptionEntity>> optionsMap) {
        PracticePaperRecordVO result = new PracticePaperRecordVO();
        generatePracticePaper(paper, questionList, categoryMap, questionMap, optionsMap, result);
        if (!result.getRecordList().isEmpty()) {
            saveBatch(result.getRecordList());
        }
        if (!result.getRecordOptionList().isEmpty()) {
            practiceRecordOptionService.saveBatch(result.getRecordOptionList());
        }
    }

    private void generatePracticePaper(PracticePaperEntity paper, List<PracticeQuestionEntity> list, Map<Long, String> categoryMap, Map<String, List<QuestionEntity>> questionMap, Map<Long, List<QuestionOptionEntity>> optionsMap, PracticePaperRecordVO result) {
        log.info("开始生成考生练习端：{}", paper.getStuId());
        list.forEach(t -> randomQuestion(t, paper, categoryMap, questionMap, optionsMap, result));
        log.info("考生练习端生成完成：{}", paper.getStuId());
    }

    private void randomQuestion(PracticeQuestionEntity entity, PracticePaperEntity paper, Map<Long, String> categoryMap, Map<String, List<QuestionEntity>> questionMap,
                                Map<Long, List<QuestionOptionEntity>> optionsMap, PracticePaperRecordVO result) {
        if (entity.getNum() == null || entity.getNum() == 0) {
            return;
        }
        String key = ExamUtil.generateQuestionKey(entity.getQuestionCategoryId(), entity.getType(), entity.getDifficulty());
        List<QuestionEntity> qustionList = CollUtil.newArrayList(questionMap.get(key));

        ExamError.QUESTION_NUM_TOO_LESS.isTrue(CollUtil.isNotEmpty(qustionList) && qustionList.size() >= entity.getNum(),
                categoryMap.get(entity.getQuestionCategoryId()), entity.getType().getDesc(), entity.getDifficulty().getDesc());

        int num = entity.getNum();

        while (num > 0) {
            int index = RandomUtil.randomInt(qustionList.size());
            QuestionEntity question = qustionList.get(index);
            log.info("试卷id：{},考生id：{}，问题：{}", entity.getPracticeId(), paper.getStuId(), question);
            PracticeRecordEntity recordEntity = convertPracticeRecordEntity(entity, question, paper);
            if (question.getType() == QuestionTypeEnum.MULTIPLE || question.getType() == QuestionTypeEnum.SINGLE) {
                PracticeOptionVO practiceOptionVO = randomQuestionOption(optionsMap.get(question.getId()), recordEntity, question.getAnswer());
                recordEntity.setAnswer(practiceOptionVO.getAnswer());
                result.getRecordOptionList().addAll(practiceOptionVO.getList());
            }
            result.getRecordList().add(recordEntity);
            qustionList.remove(question);
            num--;
        }
    }

    private PracticeOptionVO randomQuestionOption(List<QuestionOptionEntity> questionOptionEntities, PracticeRecordEntity recordEntity, String answer) {
        return ExamUtil.randomPracticeOption(questionOptionEntities, recordEntity, answer);
    }

    private PracticeRecordEntity convertPracticeRecordEntity(PracticeQuestionEntity entity, QuestionEntity question, PracticePaperEntity paper) {
        return (PracticeRecordEntity) new PracticeRecordEntity()
                .setPracticeId(paper.getPracticeId())
                .setQuestionId(question.getId())
                .setPracticePaperId(paper.getId())
                .setType(question.getType())
                .setTitle(question.getTitle())
                .setDifficulty(question.getDifficulty())
                .setAnswer(question.getAnswer())
                .setAnalyse(question.getAnalyse())
                .setScore(entity.getScore())
                .setId(IdWorker.getId());
    }

}
