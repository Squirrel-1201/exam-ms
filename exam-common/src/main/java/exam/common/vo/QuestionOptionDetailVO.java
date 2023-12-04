package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @version 1.0.0
 * @date: 2022/7/18 14:10
 * @author: yangbo
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class QuestionOptionDetailVO {
    @ApiModelProperty(value = "选项序列号")
    private String serialNo;

    @ApiModelProperty(value = "选项")
    private String option;
}
