package exam.common.vo;

import exam.common.enums.EnableEnum;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/22 17:40
 */
@Data
public class QuestionResultVO {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "题库类目id")
    private Long questionCategoryId;

    @ApiModelProperty(value = "类目名称")
    private String name;

    @ApiModelProperty(value = "0 禁用  1启用")
    private EnableEnum status;

    @ApiModelProperty(value = "问题类型：0单选，1多选，2判断")
    private QuestionTypeEnum type;

    @ApiModelProperty(value = "题干")
    private String title;

    @ApiModelProperty(value = "难易程度:0容易 1普通 2困难")
    private QuestionDifficultyEnum difficulty;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "考试错误数量")
    private Integer errorCount;

    @ApiModelProperty(value = "考试总题数量")
    private Integer totalCount;

    @ApiModelProperty(value = "失败率")
    private String failed;
}
