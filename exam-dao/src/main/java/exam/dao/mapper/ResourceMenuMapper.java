package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import exam.dao.entity.ResourceMenuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资源菜单中间表 Mapper 接口
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
public interface ResourceMenuMapper extends BaseMapper<ResourceMenuEntity> {

    void batchSave(@Param("list") List<ResourceMenuEntity> list);
}
