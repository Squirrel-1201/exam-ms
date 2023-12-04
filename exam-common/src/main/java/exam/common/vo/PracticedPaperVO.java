package exam.common.vo;

import exam.common.enums.PracticePaperStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @version 1.0.0
 * @date: 2022/7/13 19:32
 * @author: yangbo
 */
@Data
public class PracticedPaperVO {

    @ApiModelProperty(value = "唯一标识")
    private Long id;

    @ApiModelProperty(value = "考生的id")
    private Long userId;

    @ApiModelProperty(value = "考生用户名")
    private String username;

    @ApiModelProperty(value = "考生真实姓名")
    private String realName;

    @ApiModelProperty(value = "考试状态: 0 正在练习，1已完成练习，2通过，3未通过")
    private PracticePaperStatus status;

    @ApiModelProperty(value = "考试开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "考试结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "练习id")
    private Long practiceId;

    @ApiModelProperty(value = "练习名称")
    private String practiceName;

    @ApiModelProperty(value = "练习总分")
    private BigDecimal practiceScore;
}
