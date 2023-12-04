package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 用户角色中间表
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_role")
@Accessors(chain = true)
public class UserRoleEntity extends BaseEntity {

    private Long roleId;

    private Long userId;

}
