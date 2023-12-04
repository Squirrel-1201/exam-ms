package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import exam.common.enums.PracticePaperStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @version 1.0.0
 * @date: 2022/7/13 15:51
 * @author: yangbo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("practice_paper")
@ApiModel(value = "PracticePaperEntity对象", description = "练习试卷")
@Accessors(chain = true)
public class PracticePaperEntity extends BaseEntity {

    @ApiModelProperty(value = "练习关联考生id")
    private Long practiceStuId;

    @ApiModelProperty(value = "考生id")
    private Long stuId;

    @ApiModelProperty(value = "练习ID")
    private Long practiceId;

    @ApiModelProperty(value = "状态: 0 正在练习中，1 已完成练习，2通过，3未通过")
    private PracticePaperStatus status;

    @ApiModelProperty(value = "总得分")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "开始考试时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "交卷时间")
    private LocalDateTime endTime;


}
