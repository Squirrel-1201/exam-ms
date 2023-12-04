package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class SubmitExamVO {

    @ApiModelProperty(value = "试卷id", required = true)
    @NotNull(message = "试卷编号不能为空")
    private Long examId;

    @ApiModelProperty(value = "学生id", hidden = true)
    private Long stuId;

    @ApiModelProperty(value = "是否确认提交：true/false", required = true)
    private boolean confirm;
}
