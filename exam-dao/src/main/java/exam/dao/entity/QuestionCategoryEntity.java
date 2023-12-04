package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import exam.common.enums.EnableEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 问题类目
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("question_category")
@ApiModel(value="QuestionCategoryEntity对象", description="问题类目")
public class QuestionCategoryEntity extends BaseEntity {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "0 禁用  1启用")
    private EnableEnum status;

    @ApiModelProperty(value = "问题分类描述")
    private String questionDesc;

    private Long parentId;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String path;

}
