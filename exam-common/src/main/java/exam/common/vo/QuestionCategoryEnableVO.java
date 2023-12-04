package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/21 14:36
 */
@Data
public class QuestionCategoryEnableVO {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "类目名称")
    private String name;
}
