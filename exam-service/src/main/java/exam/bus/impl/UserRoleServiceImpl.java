package exam.bus.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IUserRoleService;
import exam.dao.entity.UserRoleEntity;
import exam.dao.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/24 15:53
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements IUserRoleService {

    @Override
    public List<Long> findByUserId(Long userId) {
        List<Long> list = this.listObjs(new LambdaQueryWrapper<UserRoleEntity>().eq(UserRoleEntity::getUserId, userId)
                .select(UserRoleEntity::getRoleId), roleId -> Long.valueOf(roleId.toString()));
        return Optional.ofNullable(list).orElse(CollUtil.newArrayList());
    }
}
