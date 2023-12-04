package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class SubmitAnswerVO {

    @ApiModelProperty(value = "学生id", hidden = true)
    private Long stuId;

    @ApiModelProperty(value = "试卷id", required = true)
    @NotNull(message = "试卷编号不能为空")
    private Long examId;

    @ApiModelProperty(value = "考生问题id", required = true)
    @NotNull(message = "问题编号不能为空")
    private Long recordId;

    @ApiModelProperty(value = "问题答案", required = true)
    private String answer;
}
