package exam.common.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import exam.common.serializer.LocalDateTimeMinuteJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ScoreVO {

    @ApiModelProperty(value = "试卷id")
    private Long examId;

    @ApiModelProperty(value = "试卷名称")
    private String examName;

    @ApiModelProperty(value = "考试开始时间")
    @JsonSerialize(using = LocalDateTimeMinuteJsonSerializer.class)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "考试截止时间")
    @JsonSerialize(using = LocalDateTimeMinuteJsonSerializer.class)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "试卷创建者")
    private String createBy;

    @ApiModelProperty(value = "试卷总分")
    private BigDecimal examScore;

    @ApiModelProperty(value = "通过分数")
    private BigDecimal passScore;

    @ApiModelProperty(value = "预计参考人数")
    private Integer planStuNum = 0;

    @ApiModelProperty(value = "实际参考人数")
    private Integer actualStuNum = 0;

    @ApiModelProperty(value = "通过人数")
    private Integer passStuNum = 0;

    @ApiModelProperty(value = "未通过人数")
    private Integer notPassStuNum = 0;

    @ApiModelProperty(value = "缺考人数")
    private Integer missStuNum = 0;

    @ApiModelProperty(value = "待阅卷人数")
    private Integer waitMarkingStuNum = 0;

    @ApiModelProperty(value = "阅卷中人数")
    private Integer markingStuNum = 0;

    @ApiModelProperty(value = "平均分")
    private BigDecimal avgScore = BigDecimal.ZERO;

    @ApiModelProperty(value = "最高分")
    private BigDecimal maxScore = BigDecimal.ZERO;

    @ApiModelProperty(value = "最低分")
    private BigDecimal minScore = BigDecimal.ZERO;
}
