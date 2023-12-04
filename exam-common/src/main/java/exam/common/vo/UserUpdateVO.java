package exam.common.vo;

import exam.common.constant.Constant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Set;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/24 14:41
 */
@Data
public class UserUpdateVO {

    @ApiModelProperty(value = "编号")
    @NotNull(message = "编号不能为空")
    private Long id;

    @ApiModelProperty(value = "真实姓名")
    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 2,max = 50,message = "真实姓名长度为2-50")
    private String realName;

    @ApiModelProperty(value = "组织机构id")
    @NotNull(message = "组织机构id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "角色id")
    @NotNull(message = "角色id不能为空")
    private Set<Long> roleId;

    @ApiModelProperty(value = "联系电话")
    @Size(min = 11, max = 11, message = "手机号长度11位")
    @Pattern(regexp = Constant.REGEXP_USER_PHONE,message = "手机号码格式不正确")
    private String phone;

    @ApiModelProperty(value = "联系地址")
    @Size(max = 200,message = "联系地址长度最大为200")
    private String address;

    @ApiModelProperty(value = "邮箱")
    @Size(max = 60,message = "邮箱长度最大为60")
    @Pattern(regexp = Constant.REGEXP_USER_EMAIL,message = "邮箱格式不正确")
    private String mail;
}
