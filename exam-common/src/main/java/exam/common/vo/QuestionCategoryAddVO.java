package exam.common.vo;

import exam.common.enums.EnableEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/16 10:44
 */
@Data
public class QuestionCategoryAddVO {

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "类别名称不能为空")
    @Size(max = 100,message = "类别名称最大为100")
    private String name;

    @ApiModelProperty(value = "状态: 0 禁用  1启用")
    @NotNull(message = "状态不能为空")
    private EnableEnum status;

    @ApiModelProperty(value = "问题分类描述")
    @Size(max = 100,message = "描述最大为100")
    private String questionDesc;

    @ApiModelProperty(value = "父类ID")
    @NotNull(message = "必须指定父类题目分类")
    private Long parentId;
}
