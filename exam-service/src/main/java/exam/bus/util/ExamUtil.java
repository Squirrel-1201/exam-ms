package exam.bus.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import exam.common.constant.Constant;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import exam.common.errorcode.ExamError;
import exam.dao.entity.ExamRecordEntity;
import exam.dao.entity.ExamRecordOptionEntity;
import exam.dao.entity.PracticeRecordEntity;
import exam.dao.entity.PracticeRecordOptionEntity;
import exam.dao.entity.QuestionEntity;
import exam.dao.entity.QuestionOptionEntity;
import ssm.common.util.AuthInfoUtil;

import java.time.LocalDateTime;
import java.util.*;

public class ExamUtil {

    private ExamUtil() {
    }

    //问题类目id-问题类型name()-问题难易程度name()
    private static final String QUESTION_KEY = "%s-%s-%s";

    /**
     * 获取问题map
     * <p>
     * key为 类目id-类型name()-难易程度name()
     * value 对应问题条数
     *
     * @param list
     * @return
     */
    public static Map<String, List<QuestionEntity>> getQuestionMap(List<QuestionEntity> list) {
        //问题不能为空
        ExamError.QUESTION_IS_NULL.isTrue(CollUtil.isNotEmpty(list));
        Map<String, List<QuestionEntity>> map = new HashMap<>();
        for (QuestionEntity entity : list) {
            String key = generateQuestionKey(entity.getQuestionCategoryId(), entity.getType(), entity.getDifficulty());
            if (map.containsKey(key)) {
                map.get(key).add(entity);
            } else {
                map.put(key, CollUtil.newArrayList(entity));
            }
        }
        return map;
    }


    public static String generateQuestionKey(Long categoryId, QuestionTypeEnum type, QuestionDifficultyEnum difficulty) {
        return String.format(QUESTION_KEY, categoryId, type.name(), difficulty.name());
    }


    /**
     * 随机生成考生考题选项顺序
     *
     * @param oList
     * @param recordEntity
     * @param answer
     * @return
     */
    public static ExamOptionVO getExamOption(List<QuestionOptionEntity> oList, ExamRecordEntity recordEntity, String answer) {
        Set<String> answerSet = CollUtil.newHashSet(answer.split(","));

        int size = oList.size();

        StringBuilder sb = new StringBuilder();

        List<ExamRecordOptionEntity> optionList = CollUtil.newArrayList();

        List<QuestionOptionEntity> list = CollUtil.newArrayList(oList);

        while (size > 0) {
            int index = RandomUtil.randomInt(list.size());
            QuestionOptionEntity entity = list.get(index);
            String serialNo = String.valueOf((char) (Constant.OPTION_MIN + size - 1));
            optionList.add(getOptionEntity(entity, recordEntity, serialNo));
            //判断是否为正确答案
            if (answerSet.contains(entity.getSerialNo())) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(serialNo);
            }
            list.remove(index);
            size--;
        }

        return new ExamOptionVO().setAnswer(sb.reverse().toString()).setList(optionList);
    }

    public static PracticeOptionVO randomPracticeOption(List<QuestionOptionEntity> questionOptionEntities, PracticeRecordEntity recordEntity, String answer) {
        Set<String> answerSet = CollUtil.newHashSet(answer.split(","));
        int size = questionOptionEntities.size();
        StringBuilder sb = new StringBuilder();
        List<PracticeRecordOptionEntity> optionList = new ArrayList<>();
        List<QuestionOptionEntity> list = new ArrayList<>(questionOptionEntities);

        while (size > 0) {
            int index = RandomUtil.randomInt(list.size());
            QuestionOptionEntity option = list.get(index);
            String serialNo = String.valueOf((char) (Constant.OPTION_MIN + size - 1));
            optionList.add(convertToPracticeOption(option, recordEntity, serialNo));
            if (answerSet.contains(option.getSerialNo())) {
                sb.append(serialNo).append(",");
            }
            list.remove(option);
            size--;
        }
        // 删除最后一个多余的逗号
        sb.deleteCharAt(sb.length() - 1);
        return new PracticeOptionVO().setAnswer(sb.reverse().toString()).setList(optionList);
    }

    private static PracticeRecordOptionEntity convertToPracticeOption(QuestionOptionEntity option, PracticeRecordEntity recordEntity, String serialNo) {
        return (PracticeRecordOptionEntity) new PracticeRecordOptionEntity()
                .setPracticeRecordId(recordEntity.getId())
                .setPracticeId(recordEntity.getPracticeId())
                .setPracticePaperId(recordEntity.getPracticePaperId())
                .setSerialNo(serialNo)
                .setTitle(option.getTitle())
                .setId(IdWorker.getId());
    }

    private static ExamRecordOptionEntity getOptionEntity(QuestionOptionEntity entity, ExamRecordEntity recordEntity, String serialNo) {
        return (ExamRecordOptionEntity) new ExamRecordOptionEntity()
                .setExamId(recordEntity.getExamId())
                .setTitle(entity.getTitle())
                .setStuId(recordEntity.getStuId())
                .setExamRecordId(recordEntity.getId())
                .setSerialNo(serialNo)
                .setCreateBy(AuthInfoUtil.getUserName())
                .setUpdateBy(AuthInfoUtil.getUserName())
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now())
                .setId(IdWorker.getId());
    }

}
