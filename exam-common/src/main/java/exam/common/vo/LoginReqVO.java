package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginReqVO implements Serializable {

    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "口令",required = true)
    @NotBlank(message = "口令不能为空")
    private String password;

    @ApiModelProperty(value = "验证码id",required = true)
    @NotBlank(message = "验证码id不能为空")
    private String captchaId;

    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

}
