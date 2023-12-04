package exam.dao.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import exam.common.enums.ExamStuStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 试卷和考生的关联表
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("exam_student")
@ApiModel(value="ExamStudentEntity对象", description="试卷和考生的关联表")
@Accessors(chain = true)
public class ExamStudentEntity extends BaseEntity {

    @ApiModelProperty(value = "考生id")
    private Long stuId;

    @ApiModelProperty(value = "试卷的id")
    private Long examId;

    @ApiModelProperty(value = "状态: 0 待考，1 正在考试，2已交卷，3通过，4未通过，5缺考")
    private ExamStuStatusEnum status;

    @ApiModelProperty(value = "总分")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "开始考试时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "交卷时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "考试使用IP")
    private String examIp;

}
