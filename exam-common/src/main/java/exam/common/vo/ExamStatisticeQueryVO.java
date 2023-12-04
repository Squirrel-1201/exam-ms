package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @version 1.0.0
 * @date: 2022/7/20 15:24
 * @author: yangbo
 */
@Data
public class ExamStatisticeQueryVO {
    @ApiModelProperty(value = "考试id")
    @NotNull
    private Long examId;

    @ApiModelProperty(value = "排序方式：type; difficulty; total; fail; failed; 默认使用 failed")
    private String order;

    private String sortName;

    private String sortType;

}
