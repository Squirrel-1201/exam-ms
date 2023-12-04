package exam.bus;

import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.vo.OrganizationTreeVO;
import exam.common.vo.OrganizationVO;
import exam.dao.entity.OrganizationEntity;

import java.util.List;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/22 11:42
 */
public interface IOrganizationService extends IService<OrganizationEntity> {

    /**
     * 新增组织信息
     *
     * @param vo
     */
    void add(OrganizationVO vo);

    /**
     * 修改组织信息
     *
     * @param vo
     */
    void update(OrganizationVO vo);

    /**
     * 根据id删除组织信息
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 组织机构树
     *
     * @param containsUser 是否包含用户
     *
     * @return
     */
    List<OrganizationTreeVO> tree(boolean containsUser);
}
