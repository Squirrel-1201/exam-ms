package exam.bus;

import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.vo.MenuVo;
import exam.common.vo.ResourceMenuVO;
import exam.dao.entity.MenuEntity;

import java.util.List;

public interface IMenuService extends IService<MenuEntity> {

    List<MenuVo> findMenuByUserId(Long userId);

    List<MenuVo> findMenuByRoleId(Long roleId);

    List<MenuVo> findAllMenuTree();

    void addNode(MenuVo vo);

    void update(MenuVo vo);

    void relationResource(ResourceMenuVO vo);

    void unRelationResource(ResourceMenuVO vo);
}
