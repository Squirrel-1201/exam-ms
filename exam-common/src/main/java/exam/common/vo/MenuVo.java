package exam.common.vo;

import exam.common.enums.ENVEnum;
import exam.common.enums.MenuTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class MenuVo {

    @ApiModelProperty("菜单id")
    private Long id;

    @ApiModelProperty("上级菜单id")
    private Long parentId;

    @ApiModelProperty("菜单名称")
    @Size(min = 1, max = 50, message = "菜单名称长度为1-50")
    private String menuName;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("路由id")
    @Size(max = 50, message = "菜单名称长度为不能超过50")
    private String routeId;

    @ApiModelProperty(value = "菜单类型：0 目录；1 菜单；2：路由")
    @NotNull(message = "菜单类型不能为空")
    private MenuTypeEnum type;

    @ApiModelProperty("排序号")
    private Integer orderNum;

    @ApiModelProperty(value = "0 开发环境 1 生产环境  ")
    private ENVEnum env;

    @ApiModelProperty("备注")
    @Size(max = 100, message = "备注信息长度不能超过100")
    private String remark;

    @ApiModelProperty("子菜单")
    private List<MenuVo> children = new ArrayList<>();
}
