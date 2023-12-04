package exam.bus.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IMenuService;
import exam.common.vo.MenuVo;
import exam.common.vo.ResourceMenuVO;
import exam.common.constant.Constant;
import exam.common.errorcode.CommonError;
import exam.dao.entity.MenuEntity;
import exam.dao.entity.ResourceMenuEntity;
import exam.dao.mapper.MenuMapper;
import exam.dao.mapper.ResourceMenuMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssm.common.util.AuthInfoUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements IMenuService {

    private ResourceMenuMapper resourceMenuMapper;

    /**
     * 根据用户编号获取用户菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<MenuVo> findMenuByUserId(Long userId) {
        List<MenuEntity> list = this.baseMapper.findByUserId(userId);
        return this.treeList(list);
    }

    private List<MenuVo> treeList(List<MenuEntity> list) {
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        List<MenuVo> parentList = list.stream().filter(menu -> menu.getParentId() == null)
                .map(this::convert)
                .collect(Collectors.toList());

        Map<Long, List<MenuVo>> map = list.stream().filter(menu -> menu.getParentId() != null).collect(Collectors.toMap(MenuEntity::getParentId,
                menu -> ListUtil.toList(this.convert(menu)),
                (oldList, newList) -> (List<MenuVo>) CollUtil.addAll(oldList, newList)));

        List<MenuVo> menuTreeList = CollUtil.newArrayList();
        parentList.stream().forEach(menu -> {
            menuTreeList.add(menu);
            this.menus(menu, map.get(menu.getId()), map);
        });
        return menuTreeList;
    }

    private MenuVo convert(MenuEntity menu) {
        return new MenuVo()
                .setId(menu.getId())
                .setParentId(menu.getParentId())
                .setMenuName(menu.getMenuName())
                .setIcon(menu.getIcon())
                .setRouteId(menu.getRouteId())
                .setType(menu.getType())
                .setOrderNum(menu.getOrderNum())
                .setEnv(menu.getEnv())
                .setRemark(menu.getRemark());
    }

    private void menus(MenuVo parent, List<MenuVo> list, Map<Long, List<MenuVo>> map) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        list.stream().forEach(menu -> {
            parent.getChildren().add(menu);
            this.menus(menu, map.get(menu.getId()), map);
        });
    }

    @Override
    public List<MenuVo> findMenuByRoleId(Long roleId) {
        List<MenuEntity> list = this.baseMapper.findByRoleId(roleId);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return list.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<MenuVo> findAllMenuTree() {
        List<MenuEntity> list = this.list();
        return this.treeList(list);
    }


    @Override
    @Transactional
    @CacheEvict(value = {Constant.LOGIN_RESOURCE, Constant.USER_PERM, Constant.USER_ROLE}, allEntries = true)
    public void addNode(MenuVo vo) {
        vo.setId(null);
        this.validParent(vo.getParentId());
        MenuEntity entity = new MenuEntity();
        BeanUtil.copyProperties(vo, entity);
        this.save(entity);
    }

    @Override
    @Transactional
    @CacheEvict(value = {Constant.LOGIN_RESOURCE, Constant.USER_PERM, Constant.USER_ROLE}, allEntries = true)
    public void update(MenuVo vo) {
        CommonError.NOT_NULL.notNull(vo.getId(), "id");

        MenuEntity entity = this.getById(vo.getId());
        CommonError.NOT_EXISTS.notNull(entity, "菜单信息");

        entity.setEnv(vo.getEnv())
                .setMenuName(vo.getMenuName())
                .setIcon(vo.getIcon())
                .setRemark(vo.getRemark())
                .setRouteId(vo.getRouteId())
                .setType(vo.getType())
                .setOrderNum(vo.getOrderNum());
        this.updateById(entity);
    }

    private void validParent(Long parentId) {
        if (parentId != null) {
            MenuEntity entity = this.getById(parentId);
            CommonError.NOT_EXISTS.notNull(entity, "上级菜单");
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {Constant.LOGIN_RESOURCE, Constant.USER_PERM, Constant.USER_ROLE}, allEntries = true)
    public void relationResource(ResourceMenuVO vo) {
        //清除菜单所有关联
        this.resourceMenuMapper.delete(new LambdaQueryWrapper<ResourceMenuEntity>()
                .eq(ResourceMenuEntity::getMenuId, vo.getMenuId()));
        //添加关联
        List<ResourceMenuEntity> list = vo.getResourceIds().stream().map(resourceId -> {
            ResourceMenuEntity entity = new ResourceMenuEntity();
            entity.setMenuId(vo.getMenuId());
            entity.setResourceId(resourceId);
            entity.setId(IdWorker.getId());
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(entity.getCreateTime());
            entity.setCreateBy(AuthInfoUtil.getUserName());
            entity.setUpdateBy(AuthInfoUtil.getUserName());
            return entity;
        }).collect(Collectors.toList());

        this.resourceMenuMapper.batchSave(list);
    }

    @Override
    @Transactional
    @CacheEvict(value = {Constant.LOGIN_RESOURCE, Constant.USER_PERM, Constant.USER_ROLE}, allEntries = true)
    public void unRelationResource(ResourceMenuVO vo) {
        this.resourceMenuMapper.delete(new LambdaQueryWrapper<ResourceMenuEntity>()
                .eq(ResourceMenuEntity::getMenuId, vo.getMenuId())
                .in(ResourceMenuEntity::getResourceId, vo.getResourceIds()));
    }
}
