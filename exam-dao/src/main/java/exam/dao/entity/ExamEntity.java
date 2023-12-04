package exam.dao.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import exam.common.enums.EnableEnum;
import exam.common.enums.ExamStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 试卷表
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("exam")
@ApiModel(value = "ExamEntity对象", description = "试卷表")
@Accessors(chain = true)
@ToString
public class ExamEntity extends BaseEntity {

    @ApiModelProperty(value = "试卷名称")
    private String name;

    @ApiModelProperty(value = "考试开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "考试截止时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "考试时长（分钟）")
    private Integer lastTime;

    @ApiModelProperty(value = "试卷总分")
    private BigDecimal score;

    @ApiModelProperty(value = "通过分数")
    private BigDecimal passScore;

    @ApiModelProperty(value = "状态 0 待发布  1已发布  2 阅卷中 3 已结束")
    private ExamStatus status;

    @ApiModelProperty(value = "是否自动进行阅卷状态: 0 否  1是 ")
    private EnableEnum autoMarking;

    @ApiModelProperty(value = "是否启用防作弊: 0 否  1是")
    private EnableEnum preventionCheat;
}
