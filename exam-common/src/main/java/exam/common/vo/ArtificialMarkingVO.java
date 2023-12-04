package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Auther: zhoufs
 * @Date: 2022/6/16 13:52
 * @Description:
 */
@Data
public class ArtificialMarkingVO {

    @ApiModelProperty(value = "试卷id")
    @NotNull(message = "试卷id不能为空")
    private Long examId;

    @ApiModelProperty(value = "考生id")
    @NotNull(message = "考生id不能为空")
    private Long stuId;
}
