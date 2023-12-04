package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 角色菜单表
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("role_menu")
@ApiModel(value="RoleMenuEntity对象", description="角色菜单表")
public class RoleMenuEntity extends BaseEntity {

    private Long roleId;

    private Long menuId;

}
