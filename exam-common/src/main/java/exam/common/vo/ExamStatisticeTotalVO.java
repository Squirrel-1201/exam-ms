package exam.common.vo;

import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @version 1.0.0
 * @date: 2022/7/20 15:28
 * @author: yangbo
 */
@Data
public class ExamStatisticeTotalVO {

    private Long questionId;

    private QuestionTypeEnum type;

    private QuestionDifficultyEnum difficulty;

    private String title;

    private Integer total;

    private Integer fail;

    private BigDecimal failed;
}
