package exam.common.vo;

import exam.common.enums.UserStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/24 16:20
 */
@Data
public class UserStatusUpdateVO {

    @ApiModelProperty(value = "编号")
    @NotNull(message = "编号不能为空")
    private Long id;

    @ApiModelProperty(value = "状态 0禁用  1启用  2锁定 3注销")
    @NotNull(message = "状态不能为空")
    private UserStatusEnum status;

}
