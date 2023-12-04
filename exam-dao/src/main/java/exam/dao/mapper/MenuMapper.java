package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import exam.dao.entity.MenuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
public interface MenuMapper extends BaseMapper<MenuEntity> {

    List<MenuEntity> findByUserId(@Param("userId") Long userId);

    List<MenuEntity> findByRoleId(@Param("roleId") Long roleId);
}
