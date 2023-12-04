package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 角色表
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("role")
@ApiModel(value="RoleEntity对象", description="角色表")
@Accessors(chain = true)
public class RoleEntity extends BaseEntity {

    private String roleName;

    private String remark;

}
