package exam.common.vo;

import exam.common.enums.ExamStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ssm.common.constant.DateCommonConstant;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/22 9:24
 */
@Data
public class ExamQueryVO {

    @ApiModelProperty(value = "试卷名称")
    private String name;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = DateCommonConstant.TIME_PATTERN_YYYY_MM_DD_HH_MM)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "截止时间")
    @DateTimeFormat(pattern = DateCommonConstant.TIME_PATTERN_YYYY_MM_DD_HH_MM)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "试卷创建者")
    private String createBy;

    @ApiModelProperty(value = "试卷总分")
    private BigDecimal score;

    @ApiModelProperty(value = "状态 0 待发布  1已发布  2 阅卷中 3 已结束")
    private ExamStatus status;
}
