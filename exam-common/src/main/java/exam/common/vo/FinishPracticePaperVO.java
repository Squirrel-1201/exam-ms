package exam.common.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import exam.common.enums.PracticePaperStatus;
import exam.common.serializer.LocalDateTimeMinuteJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @version 1.0.0
 * @date: 2022/7/18 17:12
 * @author: yangbo
 */
@Data
@Accessors(chain = true)
public class FinishPracticePaperVO {

    @ApiModelProperty(value = "练习试卷ID")
    private Long paperId;

    @ApiModelProperty(value = "练习用户ID")
    private Long userId;

    @ApiModelProperty(value = "练习名称")
    private String practiceName;

    @ApiModelProperty(value = "练习总分")
    private BigDecimal practiceScore;

    @ApiModelProperty(value = "通过分数")
    private BigDecimal passScore;

    @ApiModelProperty(value = "练习时长（分钟）")
    private Integer lastTime;

    @ApiModelProperty(value = "考试成绩")
    private BigDecimal stuScore;

    @ApiModelProperty(value = "开始练习时间")
    @JsonSerialize(using = LocalDateTimeMinuteJsonSerializer.class)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "练习完成时间")
    @JsonSerialize(using = LocalDateTimeMinuteJsonSerializer.class)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "练习状态")
    private PracticePaperStatus status;

}
