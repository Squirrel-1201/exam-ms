package exam.common.vo;

import exam.common.enums.EnableEnum;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/16 10:52
 */
@Data
@Accessors(chain = true)
public class QuestionVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "题库类目id")
    @NotNull(message = "题库类目不能为空")
    private Long questionCategoryId;

    @ApiModelProperty(value = "状态: 0 禁用  1启用")
    @NotNull(message = "状态不能为空")
    private EnableEnum status;

    @ApiModelProperty(value = "问题类型：0单选，1多选，2判断")
    @NotNull(message = "问题类型不能为空")
    private QuestionTypeEnum type;

    @ApiModelProperty(value = "题干")
    @NotBlank(message = "题干不能为空")
    private String title;

    @ApiModelProperty(value = "单选题、多选题选项")
    private List<String> options;

    @ApiModelProperty(value = "答案")
    private String answer;

    @ApiModelProperty(value = "解析")
    private String analyse;

    @ApiModelProperty(value = "难易程度:0容易 1普通 2困难")
    @NotNull(message = "难易程度不能为空")
    private QuestionDifficultyEnum difficulty;

}
