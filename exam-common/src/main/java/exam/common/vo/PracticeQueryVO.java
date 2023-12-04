package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @version 1.0.0
 * @date: 2022/7/13 16:54
 * @author: yangbo
 */
@Data
public class PracticeQueryVO {

    @ApiModelProperty(value = "练习名称")
    private String name;
}
