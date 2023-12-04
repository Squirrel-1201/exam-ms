package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 资源菜单中间表
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("resource_menu")
@ApiModel(value="ResourceMenuEntity对象", description="资源菜单中间表")
public class ResourceMenuEntity extends BaseEntity {

    private Long resourceId;

    private Long menuId;

}
