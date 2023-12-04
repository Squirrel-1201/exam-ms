package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import ssm.common.constant.DateCommonConstant;

import java.time.LocalDateTime;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/3/9 20:09
 */
@Data
@Accessors(chain = true)
public class ScoreQueryVO {

    @ApiModelProperty(value = "试卷名称")
    private String examName;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = DateCommonConstant.TIME_PATTERN_YYYY_MM_DD_HH_MM)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "截止时间")
    @DateTimeFormat(pattern = DateCommonConstant.TIME_PATTERN_YYYY_MM_DD_HH_MM)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "试卷创建者")
    private String createBy;

}
