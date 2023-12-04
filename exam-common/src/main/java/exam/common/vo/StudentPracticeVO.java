package exam.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import exam.common.enums.PracticeStudentStatus;
import exam.common.serializer.LocalDateTimeMinuteJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @version 1.0.0
 * @date: 2022/7/7 9:36
 * @author: yangbo
 */
@Data
@Accessors(chain = true)
public class StudentPracticeVO {

    @ApiModelProperty(value = "练习关联考生id")
    private Long id;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long userId;

    @ApiModelProperty(value = "练习id")
    private Long practiceId;

    @ApiModelProperty(value = "练习名称")
    private String practiceName;

    @ApiModelProperty(value = "练习总分")
    private BigDecimal practiceScore;

    @ApiModelProperty(value = "通过分数")
    private BigDecimal passScore;

    @ApiModelProperty(value = "练习开始时间")
    @JsonSerialize(using = LocalDateTimeMinuteJsonSerializer.class)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "练习结束时间")
    @JsonSerialize(using = LocalDateTimeMinuteJsonSerializer.class)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "练习时长（分钟）")
    private Integer lastTime;

    @ApiModelProperty(value = "总的练习次数")
    private Integer practiceTimes;

    @ApiModelProperty(value = "已练习过次数")
    private Integer usedTimes = 0;

    @ApiModelProperty(value = "状态: 0 未处在练习状态，1 正在练习中")
    private PracticeStudentStatus status;

    @ApiModelProperty(value = "统计是否正在练习")
    private Integer practiceTotal;

    /**
     * 根据是否有正在练习的试卷判断试卷状态
     */
    public void changePracticeStudentStatus() {
        if (Objects.isNull(practiceTotal) || practiceTotal == 0) {
            status = PracticeStudentStatus.UN_PRACTICE;
        } else {
            status = PracticeStudentStatus.PRACTICE_ING;
        }
    }
}
