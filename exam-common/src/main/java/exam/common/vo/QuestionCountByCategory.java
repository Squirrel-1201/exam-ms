package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @version 1.0.0
 * @date: 2022/7/14 14:53
 * @author: yangbo
 */
@Data
public class QuestionCountByCategory {

    @ApiModelProperty(value = "分类id")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "该分类下题目数量统计")
    private Integer count;
}
