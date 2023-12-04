package exam.bus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.vo.RoleMenuVO;
import exam.common.vo.RoleVO;
import exam.dao.entity.RoleEntity;
import ssm.common.entity.ComboBox;

import java.util.List;
import java.util.Set;

public interface IRoleService extends IService<RoleEntity> {

    IPage<RoleVO> pageList(RoleVO vo, Page page);

    void add(RoleVO vo);

    void update(RoleVO vo);

    void delete(Set<Long> ids);

    void setRoleMenus(RoleMenuVO vo);

    List<ComboBox> comboBox();
}
