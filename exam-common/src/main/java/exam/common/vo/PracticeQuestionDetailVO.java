package exam.common.vo;

import exam.common.enums.QuestionTypeEnum;
import exam.common.enums.YNEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @version 1.0.0
 * @date: 2022/7/18 14:10
 * @author: yangbo
 */
@Data
@Accessors(chain = true)
public class PracticeQuestionDetailVO {
    @ApiModelProperty(value = "问题类型：0单选，1多选，2判断")
    private QuestionTypeEnum type;

    @ApiModelProperty(value = "题干")
    private String title;

    @ApiModelProperty(value = "问题分数")
    private BigDecimal score;

    @ApiModelProperty(value = "选项集合")
    private List<QuestionOptionDetailVO> options;

    @ApiModelProperty(value = "考生答案")
    private String stuAnswer;

    @ApiModelProperty(value = "考生得分")
    private BigDecimal stuScore;

    @ApiModelProperty(value = "答题状态0错误，1正确")
    private YNEnum status;

    @ApiModelProperty(value = "参考答案")
    private String answer;

    @ApiModelProperty(value = "答案解析")
    private String analyse;

    @ApiModelProperty(value = "问题id")
    private Long recordId;
}
