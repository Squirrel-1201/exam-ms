package exam.common.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import exam.common.serializer.LocalDateTimeMinuteJsonDeserializer;
import exam.common.serializer.LocalDateTimeMinuteJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0.0
 * @date: 2022/7/13 10:24
 * @author: yangbo
 */
@Data
public class PracticeVO {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "练习名称")
    @NotBlank(message = "练习名称不能为空")
    @Size(min = 2,max = 50,message = "练习名称长度为2-50")
    private String name;

    @ApiModelProperty(value = "练习总分数")
    @NotNull(message = "练习总分数不能为空")
    @Min(value = 1, message = "总分数错误")
    private BigDecimal score;

    @ApiModelProperty(value = "通过分数")
    @NotNull(message = "通过分数不能为空")
    @Min(value = 1, message = "通过分数错误")
    private BigDecimal passScore;

    @ApiModelProperty(value = "练习时长（分钟）")
    @NotNull(message = "练习时长不能为空")
    @Min(value = 1, message = "考试时长错误")
    private Integer lastTime;

    @ApiModelProperty(value = "练习开始时间")
    @JsonSerialize(using = LocalDateTimeMinuteJsonSerializer.class)
    @JsonDeserialize(using = LocalDateTimeMinuteJsonDeserializer.class)
    @NotNull(message = "练习开始时间不能为空")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "练习截止时间")
    @JsonSerialize(using = LocalDateTimeMinuteJsonSerializer.class)
    @JsonDeserialize(using = LocalDateTimeMinuteJsonDeserializer.class)
    @NotNull(message = "练习截止时间不能为空")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "练习次数")
    @NotNull(message = "练习次数不允许为空")
    @Min(value = 1,message = "练习次数不小于1")
    private Integer times;

    @ApiModelProperty(value = "考试问题集合")
    @NotEmpty(message = "练习问题不能为空")
    private List<PracticeQuestionVO> practiceQuestions;

    public void trimName(){
        this.name = name.trim();
    }
}
