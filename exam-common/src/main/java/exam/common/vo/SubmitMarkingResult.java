package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther: zhoufs
 * @Date: 2022/6/13 14:27
 * @Description: 人工阅卷信息
 */
@Data
public class SubmitMarkingResult {

    @ApiModelProperty(value = "学生id", required = true)
    @NotNull(message = "学生编号不能为空")
    private Long stuId;

    @ApiModelProperty(value = "试卷id", required = true)
    @NotNull(message = "试卷编号不能为空")
    private Long examId;

    @ApiModelProperty(value = "阅卷分数信息", required = true)
    @NotEmpty(message = "阅卷分数信息不能为空")
    private List<SubmitMarkingScore> markingScoreList;
}
