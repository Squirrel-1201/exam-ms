package exam.common.vo;

import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ExamQuestionVO {

    private Long examId;

    @ApiModelProperty(value = "类目id")
    @NotNull(message = "试题类别不能为空")
    private Long questionCategoryId;

    /**
     * 问题类型：0单选，1多选，2判断
     */
    @ApiModelProperty(value = "问题类型：0单选，1多选，2判断")
    @NotNull(message = "问题类型不能为空")
    private QuestionTypeEnum type;

    /**
     * 试题难度 0简单 1普通 2困难
     */
    @ApiModelProperty(value = "试题难度：0简单 1普通 2困难")
    @NotNull(message = "试题难度不能为空")
    private QuestionDifficultyEnum difficulty;

    @ApiModelProperty(value = "题目数量")
    @NotNull(message = "题目数量不能为空")
    private Integer num;

    @ApiModelProperty(value = "试题分数")
    @NotNull(message = "试题分数不能为空")
    private BigDecimal score;
}
