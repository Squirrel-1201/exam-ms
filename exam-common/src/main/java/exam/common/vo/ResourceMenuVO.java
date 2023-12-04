package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class ResourceMenuVO {

    @ApiModelProperty(value = "菜单id")
    @NotNull(message = "菜单id不能为空")
    private Long menuId;

    @ApiModelProperty(value = "资源id集合")
    @NotNull
    private Set<Long> resourceIds;
}
