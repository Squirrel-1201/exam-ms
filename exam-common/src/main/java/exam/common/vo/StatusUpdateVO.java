package exam.common.vo;

import exam.common.enums.EnableEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: zhoufs
 * @Description:  启用或禁用时请求对象
 * @date 2022/2/21 15:24
 */
@Data
public class StatusUpdateVO {

    @ApiModelProperty(value = "编号")
    @NotNull(message = "编号不能为空")
    private Long id;

    @ApiModelProperty(value = "状态: 0 禁用  1启用")
    @NotNull(message = "状态不能为空")
    private EnableEnum status;
}
