package exam.common.vo;

import exam.common.enums.UserStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/24 17:02
 */
@Data
public class UserVO {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "状态 0禁用  1启用  2锁定 3注销")
    private UserStatusEnum status;

    @ApiModelProperty(value = "组织机构id")
    private Long orgId;

    @ApiModelProperty(value = "组织机构层级关系id")
    private String orgPath;

    @ApiModelProperty(value = "组织机构名称")
    private String orgName;

    @ApiModelProperty(value = "角色id")
    private Set<Long> roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "最后一次登录时间")
    private LocalDateTime loginLast;
}
