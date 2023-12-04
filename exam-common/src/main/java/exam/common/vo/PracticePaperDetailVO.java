package exam.common.vo;

import exam.common.enums.PracticePaperStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0
 * @date: 2022/7/18 13:55
 * @author: yangbo
 */
@Data
@Accessors(chain = true)
public class PracticePaperDetailVO {
    @ApiModelProperty(value = "考试剩余时间（秒）")
    private Long remainingSecond;

    @ApiModelProperty(value = "考生用户名")
    private String username;

    @ApiModelProperty(value = "考生真实姓名")
    private String realName;

    @ApiModelProperty(value = "练习试卷状态")
    private PracticePaperStatus paperStatus;

    @ApiModelProperty(value = "练习试卷的id")
    private Long practicePaperId;

    @ApiModelProperty(value = "练习名称")
    private String practiceName;

    @ApiModelProperty(value = "练习总分")
    private BigDecimal practiceScore;

    @ApiModelProperty(value = "考生得分")
    private BigDecimal stuScore;

    @ApiModelProperty(value = "练习题集合")
    private List<PracticeQuestionDetailVO> list = new ArrayList<>();

}
