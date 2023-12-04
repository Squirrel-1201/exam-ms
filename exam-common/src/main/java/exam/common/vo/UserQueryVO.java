package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/24 16:29
 */
@Data
public class UserQueryVO {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "组织机构id")
    private Long orgId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;
}
