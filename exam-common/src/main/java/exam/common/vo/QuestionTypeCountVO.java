package exam.common.vo;

import cn.hutool.core.collection.CollUtil;
import exam.common.enums.QuestionTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/3/3 15:57
 */
@Data
@Accessors(chain = true)
public class QuestionTypeCountVO {

    @ApiModelProperty(value = "题库类目id")
    private Long questionCategoryId;

    @ApiModelProperty(value = "问题类型：0单选，1多选，2判断")
    private QuestionTypeEnum questionType;

    @ApiModelProperty(value = "问题类型总数量")
    private Integer totalNum = 0;

    @ApiModelProperty(value = "问题各类型难易程度统计集合")
    private List<QuestionEnableCountVO> difficultyCount = CollUtil.newArrayList();
}
