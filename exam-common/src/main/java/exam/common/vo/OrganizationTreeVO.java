package exam.common.vo;

import cn.hutool.core.collection.CollUtil;
import exam.common.enums.OrgTreeTypeEnum;
import exam.common.enums.UserStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/22 11:40
 */
@Data
@Accessors(chain = true)
public class OrganizationTreeVO {

    @ApiModelProperty("数id(唯一)")
    private String treeId;

    @ApiModelProperty(value = "机构id/用户id")
    private Long id;

    @ApiModelProperty(value = "机构名称/用户真实名称")
    private String name;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "父节点id")
    private Long parentId;

    @ApiModelProperty(value = "组织机构排序号")
    @NotNull(message = "排序号不能为空")
    private Integer orderNum;

    @ApiModelProperty(value = "类型: 0 组织机构，1 用户")
    private OrgTreeTypeEnum type;

    @ApiModelProperty(value = "用户状态0禁用  1启用  2锁定 3注销")
    private UserStatusEnum userStatus;

    @ApiModelProperty(value = "下级机构/用户信息")
    private List<OrganizationTreeVO> children = CollUtil.newArrayList();
}
