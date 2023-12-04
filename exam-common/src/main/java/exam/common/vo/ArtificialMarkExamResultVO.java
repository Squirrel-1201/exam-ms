package exam.common.vo;

import exam.common.enums.EnableEnum;
import exam.common.enums.ExamStatus;
import exam.common.enums.ExamStuStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Auther: zhoufs
 * @Date: 2022/6/16 14:38
 * @Description:  阅卷管理列表结果对象
 */

@Data
public class ArtificialMarkExamResultVO {

    @ApiModelProperty(value = "唯一标识")
    private Long id;

    @ApiModelProperty(value = "考生的id")
    private Long userId;

    @ApiModelProperty(value = "考生用户名")
    private String username;

    @ApiModelProperty(value = "考生真实姓名")
    private String realName;

    @ApiModelProperty(value = "考试状态: 0 待考，1正在考试，2已交卷，3通过，4未通过，5缺考 6阅卷中")
    private ExamStuStatusEnum examStuStatus;

    @ApiModelProperty(value = "考试开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "考试结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "试卷的id")
    private Long examId;

    @ApiModelProperty(value = "试卷的名称")
    private String examName;

    @ApiModelProperty(value = "试卷状态：0 未发布 1已发布 2阅卷中 3已结束")
    private ExamStatus examStatus;

    @ApiModelProperty(value = "试卷总分")
    private BigDecimal examScore;

    @ApiModelProperty(value = "是否自动进行阅卷状态: 0 否  1 是 ")
    private EnableEnum autoMarking;

}
