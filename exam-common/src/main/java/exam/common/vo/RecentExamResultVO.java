package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * @version 1.0.0
 * @date: 2022/7/14 15:53
 * @author: yangbo
 */
@Data
public class RecentExamResultVO {

    @ApiModelProperty(value = "试卷id")
    private Long id;

    @ApiModelProperty(value = "试卷名称")
    private String name;

    @ApiModelProperty(value = "参与考试总的人员")
    private Integer total;

    @ApiModelProperty(value = "考试通过人员")
    private Integer passCount;

    @ApiModelProperty(value = "通过率")
    private String passed;

    public void computePassed() {
        if (Objects.isNull(passCount)) {
            passCount = 0;
        }
        if (Objects.isNull(total) || total == 0) {
            total = 0;
            passed = "0%";
            return;
        }
        passed = String.format("%.2f%%", (double) passCount / (double) total * 100);
    }
}
