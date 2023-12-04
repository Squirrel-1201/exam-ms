package exam.common.vo;

import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: zhoufs
 * @Description:  问题各类型难易程度对象
 * @date 2022/2/23 14:14
 */
@Data
@Accessors(chain = true)
public class QuestionEnableCountVO {

    @ApiModelProperty(value = "题库类目id")
    private Long questionCategoryId;

    @ApiModelProperty(value = "问题类型：0单选，1多选，2判断")
    private QuestionTypeEnum type;

    @ApiModelProperty(value = "问题数量")
    private Integer questionNum;

    @ApiModelProperty(value = "问题难易程度：0简单，1普通，2困难")
    private QuestionDifficultyEnum difficulty;

}
