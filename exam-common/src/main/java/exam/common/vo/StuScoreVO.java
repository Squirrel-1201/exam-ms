package exam.common.vo;

import exam.common.enums.ExamStuStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class StuScoreVO {

    @ApiModelProperty(value = "试卷id")
    private Long examId;

    @ApiModelProperty(value = "试卷名称")
    private String examName;

    @ApiModelProperty(value = "考生id")
    private Long userId;

    @ApiModelProperty(value = "考生用户名")
    private String username;

    @ApiModelProperty(value = "考生真实姓名")
    private String realName;

    @ApiModelProperty(value = "组织机构名称")
    private String orgName;

    @ApiModelProperty(value = "组织机构id")
    private Long orgId;

    @ApiModelProperty(value = "成绩")
    private BigDecimal score;

    @ApiModelProperty(value = "状态")
    private ExamStuStatusEnum status;

}
