package exam.common.vo;

import exam.common.enums.EnableEnum;
import exam.common.enums.YNEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0
 * @date: 2022/7/5 10:10
 * @author: yangbo
 */
@Data
@Accessors(chain = true)
public class QuestionCategoryTreeVO {

    @ApiModelProperty("问题策略主键id")
    private Long id;

    @ApiModelProperty("树节点id")
    private String treeId;

    @ApiModelProperty("是否题目类型节点")
    private YNEnum leaf;

    @ApiModelProperty("父节点id")
    private Long parentId;

    @ApiModelProperty("父节点id")
    private String name;

    private String type;

    private EnableEnum status;

    private Integer count = 0;

    private List<QuestionCategoryTreeVO> child = new ArrayList<>();

}
