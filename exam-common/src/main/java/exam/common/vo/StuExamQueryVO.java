package exam.common.vo;

import exam.common.enums.ExamStatus;
import exam.common.enums.ExamStuStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: zhoufs
 * @Date: 2022/6/17 16:34
 * @Description:
 */
@Data
public class StuExamQueryVO {

    @ApiModelProperty(value = "考生用户名")
    private String username;

    @ApiModelProperty(value = "考生真实姓名")
    private String realName;

    @ApiModelProperty(value = "试卷的名称")
    private String examName;

    @ApiModelProperty(value = "考试状态: 0 待考，1正在考试，2已交卷，3通过，4未通过，5缺考 6阅卷中")
    private ExamStuStatusEnum examStuStatus;

    @ApiModelProperty(value = "试卷状态：0 未发布 1已发布 2阅卷中 3已结束")
    private ExamStatus examStatus;
}
