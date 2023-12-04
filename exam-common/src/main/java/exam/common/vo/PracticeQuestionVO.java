package exam.common.vo;

import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @version 1.0.0
 * @date: 2022/7/13 19:11
 * @author: yangbo
 */
@Data
public class PracticeQuestionVO {

    private Long practiceId;

    private Long questionCategoryId;

    /**
     * 问题类型：0单选，1多选，2判断
     */
    private QuestionTypeEnum type;

    /**
     * 试题难度 0简单 1普通 2困难
     */
    private QuestionDifficultyEnum difficulty;

    private Integer num;

    private BigDecimal score;


}
