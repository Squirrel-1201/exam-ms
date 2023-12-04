package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import exam.common.enums.PracticeStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @version 1.0.0
 * @date: 2022/7/13 10:40
 * @author: yangbo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("practice")
@ApiModel(value = "PracticeEntity对象", description = "练习表")
@Accessors(chain = true)
public class PracticeEntity extends BaseEntity {

    @ApiModelProperty(value = "练习名称")
    private String name;

    @ApiModelProperty(value = "练习开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "练习截止时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "练习时长（分钟）")
    private Integer lastTime;

    @ApiModelProperty(value = "试卷总分")
    private BigDecimal score;

    @ApiModelProperty(value = "通过分数")
    private BigDecimal passScore;

    @ApiModelProperty(value = "状态 0 待发布  1已发布  2 已结束")
    private PracticeStatus status;

    @ApiModelProperty(value = "允许练习次数")
    private Integer times;
}
