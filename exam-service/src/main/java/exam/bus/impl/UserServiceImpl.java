package exam.bus.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IOrganizationService;
import exam.bus.IRoleService;
import exam.bus.IUserRoleService;
import exam.bus.IUserService;
import exam.common.config.UserLoginConfig;
import exam.common.constant.Constant;
import exam.common.enums.UserStatusEnum;
import exam.common.errorcode.AuthError;
import exam.common.errorcode.CommonError;
import exam.common.errorcode.UserError;
import exam.common.vo.*;
import exam.dao.entity.OrganizationEntity;
import exam.dao.entity.RoleEntity;
import exam.dao.entity.UserEntity;
import exam.dao.entity.UserRoleEntity;
import exam.dao.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssm.common.captcha.CaptchaService;
import ssm.common.entity.BaseEntity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {

    private CaptchaService service;

    private UserLoginConfig loginConfig;

    private IOrganizationService organizationService;

    private IRoleService roleService;

    private IUserRoleService userRoleService;

    @Override
    @Cacheable(value = Constant.USER_PERM, key = "#userId", unless = "#result == null ")
    public Set<String> getPerms(Long userId) {
        return this.baseMapper.getPerms(userId);
    }

    @Override
    public LoginResVO login(LoginReqVO vo) {
        boolean valid = this.service.isValid(vo.getCaptchaId(), vo.getCode());
        AuthError.CAPTCHA_CODE_ERROR.isTrue(valid);
        UserEntity user = this.getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, vo.getUsername()));
        AuthError.USER_NOT_FUND.notNull(user);
        //校验密码
        this.validPwd(vo.getPassword(), user);
        //校验状态
        this.validStatus(user);

        user.setLoginFailCount(0);
        user.setLockTime(null);
        user.setStatus(UserStatusEnum.ENABLE);
        user.setLoginLast(LocalDateTime.now());
        this.updateById(user);

        List<Long> roleIds = userRoleService.findByUserId(user.getId());
        return new LoginResVO().setUserId(user.getId()).setUsername(user.getUsername()).setRole(StrUtil.join(",", roleIds.toArray()));
    }

    @Override
    @Transactional
    public void addUser(UserAddVO vo) {
        // 校验用户名
        UserError.USER_NAME_HAD_EXIST.isTrue(checkUserName(null, vo.getUsername().trim()));
        // 校验组织机构
        checkOrgId(vo.getOrgId());
        // 校验用户角色
        checkRoleId(vo.getRoleId());
        // 保存用户信息
        UserEntity userEntity = new UserEntity();
        vo.setUsername(vo.getUsername().trim());
        vo.setRealName(vo.getRealName().trim());
        BeanUtils.copyProperties(vo, userEntity);
        // 创建用户默认启用状态
        userEntity.setStatus(UserStatusEnum.ENABLE);
        // 用户密码加密处理

        save(userEntity);

        // 保存用户和角色关系
        List<UserRoleEntity> list = vo.getRoleId().stream().map(roleId -> new UserRoleEntity().setRoleId(roleId).setUserId(userEntity.getId())).collect(Collectors.toList());
        this.userRoleService.saveBatch(list);
    }

    @Override
    @Transactional
    @CacheEvict(value = {Constant.USER_PERM,Constant.USER_ROLE}, allEntries = true)
    public String updateUser(UserUpdateVO vo) {
        if (vo.getId() == null) {
            UserError.REQUEST_PRAM_ERROR.assertThrow();
        }
        // 校验用户id
        UserEntity entity = checkUserId(vo.getId());
        // 校验组织机构
        checkOrgId(vo.getOrgId());
        // 校验用户角色
        checkRoleId(vo.getRoleId());
        // 修改用户信息
        UserEntity userEntity = new UserEntity();
        vo.setRealName(vo.getRealName().trim());
        BeanUtils.copyProperties(vo, userEntity);
        saveOrUpdate(userEntity);
        // 删除用户和角色关系
        this.userRoleService.remove(new LambdaUpdateWrapper<UserRoleEntity>().eq(UserRoleEntity::getUserId, userEntity.getId()));

        // 保存用户和角色关系
        List<UserRoleEntity> list = vo.getRoleId().stream().map(roleId -> new UserRoleEntity().setRoleId(roleId).setUserId(userEntity.getId())).collect(Collectors.toList());
        this.userRoleService.saveBatch(list);
        return entity.getUsername();
    }

    @Override
    public String changeUserStatus(UserStatusUpdateVO vo) {
        UserEntity entity = this.checkUserId(vo.getId());
        // 注销用户不可启用
        UserError.USER_ENABLE_ERROR.isFalse(UserStatusEnum.CANCEL == getById(vo.getId()).getStatus());
        // 启用或禁用用户
        UserError.STATUS_PARAM_ERROR.isTrue(UserStatusEnum.DISABLE == vo.getStatus() || UserStatusEnum.ENABLE == vo.getStatus());
        this.update(new LambdaUpdateWrapper<UserEntity>().set(UserEntity::getStatus, vo.getStatus())
                .set(UserStatusEnum.ENABLE == vo.getStatus(), UserEntity::getLoginFailCount, 0)
                .eq(BaseEntity::getId, vo.getId()));
        return entity.getUsername();
    }

    @Override
    @CacheEvict(value = {Constant.USER_PERM,Constant.USER_ROLE}, allEntries = true)
    public String cancelUser(Long id) {
        UserEntity entity = this.checkUserId(id);
        this.update(new LambdaUpdateWrapper<UserEntity>().set(UserEntity::getStatus, UserStatusEnum.CANCEL).eq(BaseEntity::getId, id));
        return entity.getUsername();
    }

    @Override
    @CacheEvict(value = {Constant.USER_PERM,Constant.USER_ROLE}, allEntries = true)
    public void cancelBatchUser(Set<Long> ids) {
        CommonError.NOT_NULL.notEmpty(ids, "id");
        this.update(new LambdaUpdateWrapper<UserEntity>().set(UserEntity::getStatus, UserStatusEnum.CANCEL).in(BaseEntity::getId, ids));
    }

    @Override
    public String restUserPwd(Long id) {
        UserEntity entity = this.checkUserId(id);
        // 重置密码默认 123456
        this.update(new LambdaUpdateWrapper<UserEntity>().set(UserEntity::getUserPwd, this.loginConfig.getInitPwd()).eq(BaseEntity::getId, id));
        return entity.getUsername();
    }

    @Override
    public String updateUserPwd(UserUpdatePwdVO vo) {
        // 校验用户
        UserEntity userEntity = this.getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getId, vo.getId()).ne(UserEntity::getStatus, UserStatusEnum.CANCEL));
        UserError.USER_NOT_EXIST.notNull(userEntity);
        // 校验原密码
        UserError.USER_OLD_PWD_ERROR.isTrue(userEntity.getUserPwd().equals(vo.getUserPwd()));
        // 保存新密码
        this.update(new LambdaUpdateWrapper<UserEntity>().set(UserEntity::getUserPwd, vo.getUserNewPwd()).eq(BaseEntity::getId, vo.getId()));

        return userEntity.getUsername();

    }

    /**
     * 校验用户id
     *
     * @param userId
     */
    private UserEntity checkUserId(Long userId) {
        UserEntity entity = this.getById(userId);
        UserError.USER_NOT_EXIST.notNull(entity);
        return entity;
    }

    /**
     * 修改用户组织机构
     *
     * @param vo
     */
    @Override
    public String changeOrg(ChangeUserOrgVO vo) {
        //验证用户
        UserEntity entity = this.checkUserId(vo.getUserId());
        //验证组织机构
        this.checkOrgId(vo.getOrgId());
        //修改用户组织机构
        this.update(new LambdaUpdateWrapper<UserEntity>().set(UserEntity::getOrgId, vo.getOrgId()).eq(BaseEntity::getId, vo.getUserId()));
        return entity.getUsername();
    }

    @Override
    public UserVO findUserInfoById(Long id) {
        UserEntity userEntity = getById(id);
        UserError.USER_NOT_EXIST.notNull(userEntity);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userEntity, userVO);
        // 添加id
        userVO.setId(userEntity.getId());
        OrganizationEntity org = this.organizationService.getById(userVO.getOrgId());
        // 填充组织机构层级关系id
        userVO.setOrgPath(org.getPath() + "/" + userVO.getOrgId());
        // 填充组织机构名称
        userVO.setOrgName(org.getName());
        // 获取角色名称(用户存在多个角色时进行字符拼接)
        List<UserRoleEntity> userRoleEntities =
                this.userRoleService.getBaseMapper().selectList(new LambdaQueryWrapper<UserRoleEntity>().select().eq(UserRoleEntity::getUserId, userVO.getId()));

        Set<Long> roleIds = new HashSet<>();
        StringBuilder roleName = new StringBuilder();
        if (CollUtil.isNotEmpty(userRoleEntities)) {
            for (UserRoleEntity userRoleEntity : userRoleEntities) {
                RoleEntity roleEntity = this.roleService.getById(userRoleEntity.getRoleId());
                if (roleEntity != null) {
                    roleName.append(roleName).append(",").append(roleEntity.getRoleName());
                    roleIds.add(userRoleEntity.getRoleId());
                }
            }
        }
        // 填充角色id和名称名称
        userVO.setRoleId(roleIds);
        userVO.setRoleName(roleName.substring(1));
        return userVO;
    }

    @Override
    public Page<UserResponseVO> pageListByMapper(UserRequestQueryVO vo, Page page) {
        return this.baseMapper.pageList(vo, page);
    }

    private void validPwd(String password, UserEntity user) {
        if (CharSequenceUtil.equals(password, user.getUserPwd())) {
            return;
        }
        int failCount = Optional.ofNullable(user.getLoginFailCount()).orElse(0) + 1;
        user.setLoginFailCount(failCount);
        if (failCount >= this.loginConfig.getFailCount()) {
            user.setStatus(UserStatusEnum.LOCK);
            user.setLockTime(LocalDateTimeUtil.now());
            this.updateById(user);
            LocalDateTime lockToTime = user.getLockTime().plusMinutes(this.loginConfig.getLockMinutes());
            AuthError.USER_LOCK.assertThrow(DateUtil.format(lockToTime, DatePattern.NORM_DATETIME_MINUTE_PATTERN));
        }
        this.updateById(user);
        AuthError.PASSWORD_ERROR.assertThrow(this.loginConfig.getFailCount() - failCount);
    }

    private void validStatus(UserEntity user) {
        AuthError.USER_DISABLE.isFalse(user.getStatus() == UserStatusEnum.DISABLE);
        AuthError.USER_CANCEL.isFalse(user.getStatus() == UserStatusEnum.CANCEL);
        if (user.getStatus() == UserStatusEnum.LOCK) {
            LocalDateTime lockToTime = user.getLockTime().plusMinutes(this.loginConfig.getLockMinutes());
            //判断当前时间是否已到解锁时间
            long minutes = Duration.between(lockToTime, LocalDateTime.now()).toMinutes();
            AuthError.USER_LOCK.isTrue(minutes > 0, DateUtil.format(lockToTime, DatePattern.NORM_DATETIME_MINUTE_PATTERN));
        }
    }

    /**
     * 校验用户名
     *
     * @param id
     * @param username
     * @return true 通过  false 不通过
     */
    private boolean checkUserName(Long id, String username) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (id != null) {
            queryWrapper.ne(UserEntity::getId, id);
        }
        queryWrapper.eq(UserEntity::getUsername, username);
        int count = this.count(queryWrapper);
        return count == 0;
    }

    /**
     * 校验组织机构
     *
     * @param orgId
     */
    private void checkOrgId(Long orgId) {
        OrganizationEntity organizationEntity = this.organizationService.getById(orgId);
        UserError.ORGANIZATION_NOT_EXIST.notNull(organizationEntity);
    }

    /**
     * 校验用户角色
     *
     * @param roleId
     */
    private void checkRoleId(Set<Long> roleId) {
        List<RoleEntity> roleEntities = this.roleService.getBaseMapper().selectBatchIds(roleId);
        if (roleEntities.size() != roleId.size()) {
            UserError.USER_ROLE_NOT_EXIST.assertThrow();
        }
    }
}
