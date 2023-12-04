package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/22 11:40
 */
@Data
public class OrganizationVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "组织名称")
    @Size(min = 1, max = 50, message = "组织名称长度为1-50")
    @NotBlank(message = "组织名称不能为空")
    private String name;

    @ApiModelProperty(value = "组织父节点id")
    private Long parentId;

    @ApiModelProperty(value = "排序号")
    @NotNull(message = "排序号不能为空")
    private Integer orderNum;
}
