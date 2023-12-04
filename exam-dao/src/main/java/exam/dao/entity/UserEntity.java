package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import exam.common.enums.UserStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
@ApiModel(value="UserEntity对象", description="用户表")
public class UserEntity extends BaseEntity {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "密码")
    private String userPwd;

    @ApiModelProperty(value = "状态 0禁用  1启用  2锁定 3注销")
    private UserStatusEnum status;

    @ApiModelProperty(value = "组织机构id")
    private Long orgId;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "连续登录错误次数")
    private Integer loginFailCount;

    @ApiModelProperty(value = "锁定时间")
    private LocalDateTime lockTime;

    @ApiModelProperty(value = "最后一次登录时间")
    private LocalDateTime loginLast;

}
