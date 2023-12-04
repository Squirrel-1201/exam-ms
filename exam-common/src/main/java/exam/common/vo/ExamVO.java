package exam.common.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import exam.common.enums.EnableEnum;
import exam.common.serializer.LocalDateTimeMinuteJsonDeserializer;
import exam.common.serializer.LocalDateTimeMinuteJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: zhoufs
 * @Description: 创建考试试题请求对象
 * @date 2022/2/18 11:26
 */
@Data
public class ExamVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "试卷名称")
    @NotBlank(message = "试卷名称不能为空")
    @Size(min = 2,max = 50,message = "试卷名称长度为2-50")
    private String name;

    @ApiModelProperty(value = "考试开始时间")
    @JsonSerialize(using = LocalDateTimeMinuteJsonSerializer.class)
    @JsonDeserialize(using = LocalDateTimeMinuteJsonDeserializer.class)
    @NotNull(message = "考试开始时间不能为空")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "考试截止时间")
    @JsonSerialize(using = LocalDateTimeMinuteJsonSerializer.class)
    @JsonDeserialize(using = LocalDateTimeMinuteJsonDeserializer.class)
    @NotNull(message = "考试截止时间不能为空")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "考试时长（分钟）")
    @NotNull(message = "考试时长不能为空")
    @Min(value = 1, message = "考试时长错误")
    private Integer lastTime;

    @ApiModelProperty(value = "试卷总分")
    @NotNull(message = "试卷总分不能为空")
    @Min(value = 1, message = "总分数错误")
    private BigDecimal score;

    @ApiModelProperty(value = "通过分数")
    @NotNull(message = "通过分数不能为空")
    @Min(value = 1, message = "通过分数错误")
    private BigDecimal passScore;

    @ApiModelProperty(value = "考试问题集合")
    @NotEmpty(message = "考试问题不能为空")
    private List<ExamQuestionVO> examQuestions;

    @ApiModelProperty(value = "是否启用防作弊: 0 否  1是")
    private EnableEnum preventionCheat = EnableEnum.DISABLE;
}
