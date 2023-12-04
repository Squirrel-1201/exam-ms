package exam.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import exam.common.enums.ExamStuStatusEnum;
import exam.common.enums.YNEnum;
import exam.common.serializer.LocalDateTimeMinuteJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class StudentExamVO {

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long userId;

    @ApiModelProperty(value = "试卷的id")
    private Long examId;

    @ApiModelProperty(value = "试卷的名称")
    private String examName;

    @ApiModelProperty(value = "试卷总分")
    private BigDecimal examScore;

    @ApiModelProperty(value = "通过分数")
    private BigDecimal passScore;

    @ApiModelProperty(value = "考试时长（分钟）")
    private Integer lastTime;

    @ApiModelProperty(value = "考试成绩")
    private BigDecimal stuScore;

    @ApiModelProperty(value = "开始考试时间")
    @JsonSerialize(using = LocalDateTimeMinuteJsonSerializer.class)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "考试截止时间")
    @JsonSerialize(using = LocalDateTimeMinuteJsonSerializer.class)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "是否可以考试 0 否  1是")
    private YNEnum canExam;

    @ApiModelProperty(value = "状态: 0 待考，1正在考试，2已交卷，3通过，4未通过，5缺考")
    private ExamStuStatusEnum status;

    @ApiModelProperty(value = "开始考试时间")
    private LocalDateTime startStuTime;

    @ApiModelProperty(value = "交卷时间")
    private LocalDateTime endStuTime;

}
