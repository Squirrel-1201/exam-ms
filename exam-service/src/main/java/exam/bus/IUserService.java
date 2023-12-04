package exam.bus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.vo.*;
import exam.dao.entity.UserEntity;

import java.util.Set;

public interface IUserService extends IService<UserEntity> {

    Set<String> getPerms(Long userId);

    LoginResVO login(LoginReqVO vo);

    /**
     * 创建用户
     *
     * @param vo
     */
    void addUser(UserAddVO vo);

    /**
     * 修改用户
     *
     * @param vo
     */
    String updateUser(UserUpdateVO vo);

    /**
     * 启用或禁用 用户
     *
     * @param vo
     */
    String changeUserStatus(UserStatusUpdateVO vo);

    /**
     * 注销用户
     *
     * @param id
     */
    String cancelUser(Long id);

    /**
     * 批量注销用户
     *
     * @param ids
     */
    void cancelBatchUser(Set<Long> ids);

    /**
     * 重置用户密码(口令)
     *
     * @param id
     */
    String restUserPwd(Long id);

    /**
     * 用户修改密码
     * @param vo
     */
    String updateUserPwd(UserUpdatePwdVO vo);

    /**
     * 修改用户组织机构
     *
     * @param vo
     */
    String changeOrg(ChangeUserOrgVO vo);

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    UserVO findUserInfoById(Long id);

    /**
     * 分页条件查询 (通过xml进行表的关联查询)
     *
     * @param vo
     * @param page
     * @return
     */
    Page<UserResponseVO> pageListByMapper(UserRequestQueryVO vo, Page page);

}
