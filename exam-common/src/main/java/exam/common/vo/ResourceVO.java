package exam.common.vo;

import exam.common.enums.YNEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class ResourceVO {

    private Long id;

    @ApiModelProperty("资源名称")
    @Size(min = 1, max = 50, message = "资源名称长度为1-50")
    private String resourceName;

    @ApiModelProperty("url")
    @Size(min = 1, max = 100, message = "资源名称长度为1-100")
    private String url;

    @ApiModelProperty("是否需要登录0否 1是")
    @NotNull(message = "请选择是否是需要登录")
    private YNEnum loginVerify;

    @ApiModelProperty("备注")
    @Size(max = 100, message = "备注长度不能超过100")
    private String remark;
}
