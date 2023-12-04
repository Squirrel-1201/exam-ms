package exam.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/3/3 19:14
 */
@Data
@Accessors(chain = true)
public class UserUpdatePwdVO {

    @ApiModelProperty(value = "编号")
    @JsonIgnore
    private Long id;

    @ApiModelProperty(value = "旧密码")
    @NotBlank(message = "旧密码不能为空")
    private String userPwd;

    @ApiModelProperty(value = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String userNewPwd;
}
