package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/25 14:08
 */
@Data
public class ExamStudentAddVO {

    @ApiModelProperty(value = "考生id")
    @NotEmpty(message = "考生id不能为空")
    private Set<Long> stuId;

    @ApiModelProperty(value = "试卷的id")
    @NotNull(message = "试卷的id不能为空")
    private Long examId;
}
