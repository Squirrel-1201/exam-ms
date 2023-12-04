package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import exam.dao.entity.RoleMenuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色菜单表 Mapper 接口
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenuEntity> {

    void batchSave(@Param("list") List<RoleMenuEntity> list);

}
