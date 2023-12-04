package exam.bus.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IRoleService;
import exam.common.vo.RoleMenuVO;
import exam.common.vo.RoleVO;
import exam.common.constant.Constant;
import exam.common.errorcode.CommonError;
import exam.dao.entity.RoleEntity;
import exam.dao.entity.RoleMenuEntity;
import exam.dao.mapper.RoleMapper;
import exam.dao.mapper.RoleMenuMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssm.common.entity.BaseEntity;
import ssm.common.entity.ComboBox;
import ssm.common.util.AuthInfoUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements IRoleService {

    private RoleMenuMapper roleMenuMapper;

    @Override
    public IPage<RoleVO> pageList(RoleVO vo, Page page) {
        LambdaQueryWrapper<RoleEntity> wrapper = new LambdaQueryWrapper<>();
        if (vo != null) {
            wrapper.like(CharSequenceUtil.isNotBlank(vo.getRoleName()), RoleEntity::getRoleName, vo.getRoleName());
        }
        wrapper.orderByDesc(BaseEntity::getUpdateTime);
        IPage<RoleEntity> iPage = this.page(page, wrapper);
        List<RoleVO> list = iPage.getRecords().stream().map(role -> {
            RoleVO roleVO = new RoleVO();
            BeanUtil.copyProperties(role, roleVO);
            return roleVO;
        }).collect(Collectors.toList());
        return new Page<RoleVO>(iPage.getCurrent(), iPage.getSize(), iPage.getTotal()).setPages(iPage.getPages()).setRecords(list);
    }

    @Override
    @Transactional
    public void add(RoleVO vo) {
        RoleEntity role = this.getOne(new LambdaQueryWrapper<RoleEntity>().eq(RoleEntity::getRoleName, vo.getRoleName()));
        CommonError.EXISTS.isNull(role, "角色名称");
        this.save(new RoleEntity().setRoleName(vo.getRoleName()).setRemark(vo.getRemark()));
    }

    @Override
    @Transactional
    @CacheEvict(value = {Constant.LOGIN_RESOURCE, Constant.USER_PERM,Constant.USER_ROLE}, allEntries = true)
    public void update(RoleVO vo) {
        CommonError.NOT_NULL.notNull(vo.getId(), "id");
        RoleEntity role = this.getOne(new LambdaQueryWrapper<RoleEntity>().eq(RoleEntity::getRoleName, vo.getRoleName()).ne(BaseEntity::getId, vo.getId()));
        CommonError.EXISTS.isNull(role, "角色名称");
        this.save((RoleEntity) new RoleEntity().setRoleName(vo.getRoleName()).setRemark(vo.getRemark()).setId(vo.getId()));
    }

    @Override
    @Transactional
    @CacheEvict(value = {Constant.LOGIN_RESOURCE, Constant.USER_PERM,Constant.USER_ROLE}, allEntries = true)
    public void delete(Set<Long> ids) {
        CommonError.NOT_NULL.notEmpty(ids, "id");
        this.removeByIds(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = {Constant.LOGIN_RESOURCE, Constant.USER_PERM,Constant.USER_ROLE}, allEntries = true)
    public void setRoleMenus(RoleMenuVO vo) {
        this.roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenuEntity>().eq(RoleMenuEntity::getRoleId, vo.getRoleId()));
        if (CollUtil.isEmpty(vo.getMenuIds())) {
            return;
        }
        List<RoleMenuEntity> list = new ArrayList<>();
        vo.getMenuIds().stream().forEach(menuId -> {
            RoleMenuEntity entity = new RoleMenuEntity();
            entity.setId(IdWorker.getId());
            entity.setRoleId(vo.getRoleId());
            entity.setMenuId(menuId);
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(entity.getCreateTime());
            entity.setCreateBy(AuthInfoUtil.getUserName());
            entity.setUpdateBy(AuthInfoUtil.getUserName());
        });
        this.roleMenuMapper.batchSave(list);
    }

    @Override
    public List<ComboBox> comboBox() {
        List<RoleEntity> list = this.list();
        return Optional.ofNullable(list).orElse(CollUtil.newArrayList()).stream()
                .map(role -> new ComboBox().setLabel(role.getRoleName()).setValue(String.valueOf(role.getId()))).collect(Collectors.toList());
    }
}
