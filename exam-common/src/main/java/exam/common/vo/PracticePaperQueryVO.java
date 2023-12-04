package exam.common.vo;

import exam.common.enums.PracticePaperStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @version 1.0.0
 * @date: 2022/7/13 19:58
 * @author: yangbo
 */
@Data
public class PracticePaperQueryVO {

    @ApiModelProperty(value = "练习名称")
    private String practiceName;
    @ApiModelProperty(value = "考生真实姓名")
    private String realName;
    @ApiModelProperty(value = "考试状态: 0 正在练习，1已完成练习，2通过，3未通过")
    private PracticePaperStatus status;
}
