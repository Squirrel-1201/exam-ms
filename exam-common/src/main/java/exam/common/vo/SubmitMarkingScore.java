package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Auther: zhoufs
 * @Date: 2022/6/13 14:29
 * @Description:  人工阅卷分数信息
 */
@Data
public class SubmitMarkingScore {

    @ApiModelProperty(value = "考生问题id", required = true)
    @NotNull(message = "问题编号不能为空")
    private Long recordId;

    @ApiModelProperty(value = "问题得分", required = true)
    @NotNull(message = "问题得分不能为空")
    private BigDecimal finalScore;
}
