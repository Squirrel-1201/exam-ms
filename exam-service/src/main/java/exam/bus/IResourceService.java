package exam.bus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.vo.ResourceVO;
import exam.dao.entity.ResourceEntity;

import java.util.List;
import java.util.Set;

public interface IResourceService extends IService<ResourceEntity> {

    IPage<ResourceVO> pageList(ResourceVO vo, Page page);

    void add(ResourceVO vo);

    void update(ResourceVO vo);

    void delete(Set<Long> ids);

    List<String> getLoginResource();

    List<ResourceVO> findByMenuId(Long menuId);
}
