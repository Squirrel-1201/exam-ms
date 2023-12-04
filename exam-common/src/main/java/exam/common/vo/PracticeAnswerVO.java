package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @version 1.0.0
 * @date: 2022/7/18 16:07
 * @author: yangbo
 */
@Data
public class PracticeAnswerVO {

    @ApiModelProperty(value = "练习试卷id", required = true)
    @NotNull(message = "练习试卷编号不能为空")
    private Long practicePaperId;

    @ApiModelProperty(value = "练习问题id", required = true)
    @NotNull(message = "练习问题编号不能为空")
    private Long recordId;

    @ApiModelProperty(value = "问题答案", required = true)
    private String answer;
}
