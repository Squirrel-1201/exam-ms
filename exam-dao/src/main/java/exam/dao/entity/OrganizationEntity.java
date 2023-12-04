package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 组织机构表
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("organization")
@Accessors(chain = true)
public class OrganizationEntity extends BaseEntity {

    private String name;

    private Long parentId;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String path;

    private Integer orderNum;

}
