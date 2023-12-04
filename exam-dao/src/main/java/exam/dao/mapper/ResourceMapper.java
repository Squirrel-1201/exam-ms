package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import exam.dao.entity.ResourceEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
public interface ResourceMapper extends BaseMapper<ResourceEntity> {

    List<ResourceEntity> findByMenuId(@Param("menuId")Long menuId);
}
