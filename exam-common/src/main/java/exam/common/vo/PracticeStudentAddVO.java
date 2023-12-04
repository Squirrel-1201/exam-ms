package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @version 1.0.0
 * @date: 2022/7/13 17:36
 * @author: yangbo
 */
@Data
public class PracticeStudentAddVO {
    @ApiModelProperty(value = "考生id")
    @NotEmpty(message = "考生id不能为空")
    private Set<Long> stuId;

    @ApiModelProperty(value = "练习id")
    @NotNull(message = "练习id不能为空")
    private Long practiceId;
}
