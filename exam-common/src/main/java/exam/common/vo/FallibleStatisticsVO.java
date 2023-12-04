package exam.common.vo;

import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @version 1.0.0
 * @date: 2022/7/14 16:55
 * @author: yangbo
 */
@Data
public class FallibleStatisticsVO {
    @ApiModelProperty(value = "问题id")
    private Long id;
    @ApiModelProperty(value = "问题题干")
    private String title;
    @ApiModelProperty(value = "问题类型")
    private QuestionTypeEnum type;
    @ApiModelProperty(value = "问题难易程度")
    private QuestionDifficultyEnum difficulty;
    @ApiModelProperty(value = "问题类别")
    private String name;
    @ApiModelProperty(value = "错误次数统计")
    private Integer errorCount;
    @ApiModelProperty(value = "总的出题次数")
    private Integer totalCount;
    @ApiModelProperty(value = "错误率")
    private String failed;

    public void failedCompute() {
        failed = String.format("%.2f%%", (double) errorCount / (double) totalCount * 100);
    }

}
