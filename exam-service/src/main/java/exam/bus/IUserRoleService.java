package exam.bus;

import com.baomidou.mybatisplus.extension.service.IService;
import exam.dao.entity.UserRoleEntity;

import java.util.List;


/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/24 15:51
 */
public interface IUserRoleService extends IService<UserRoleEntity> {

    List<Long> findByUserId(Long userId);
}
