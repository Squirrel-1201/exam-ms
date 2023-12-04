package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @version 1.0.0
 * @date: 2022/7/18 16:25
 * @author: yangbo
 */
@Data
public class SubmitPracticePaperVO {

    @ApiModelProperty(value = "练习试卷ID", required = true)
    @NotNull
    private Long practicePaperId;

    @ApiModelProperty(value = "是否确认提交：true/false", required = true)
    private boolean confirm;
}
