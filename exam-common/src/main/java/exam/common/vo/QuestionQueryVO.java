package exam.common.vo;

import exam.common.enums.EnableEnum;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import exam.common.enums.YNEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/16 17:39
 */
@Data
public class QuestionQueryVO {

    @ApiModelProperty(value = "题库类目id")
    private Long questionCategoryId;

    @ApiModelProperty(value = "0 禁用  1启用")
    private EnableEnum status;

    @ApiModelProperty(value = "问题类型：0单选，1多选，2判断")
    private QuestionTypeEnum type;

    @ApiModelProperty(value = "难易程度:0容易 1普通 2困难")
    private QuestionDifficultyEnum difficulty;

    @ApiModelProperty(value = "题干")
    private String title;

    @ApiModelProperty(value = "重复题干查询")
    private YNEnum duplicateQuery = YNEnum.NO;

}
