package exam.bus.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IOrganizationService;
import exam.common.vo.OrganizationTreeVO;
import exam.common.vo.OrganizationVO;
import exam.common.enums.OrgTreeTypeEnum;
import exam.common.enums.UserStatusEnum;
import exam.common.errorcode.CommonError;
import exam.common.errorcode.OrganizationError;
import exam.dao.entity.OrganizationEntity;
import exam.dao.entity.UserEntity;
import exam.dao.mapper.OrganizationMapper;
import exam.dao.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/22 11:52
 */
@Service
@Slf4j
@AllArgsConstructor
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, OrganizationEntity> implements IOrganizationService {

    private UserMapper userMapper;

    @Override
    public void add(OrganizationVO vo) {
        vo.setId(null);
        LambdaQueryWrapper<OrganizationEntity> query = new LambdaQueryWrapper<OrganizationEntity>().eq(OrganizationEntity::getName, vo.getName());
        OrganizationEntity entity = this.getOne(query);
        String path = this.validParent(vo.getParentId());
        OrganizationError.ORGANIZATION_NAME_EXIST.isNull(entity);

        entity = new OrganizationEntity().setName(vo.getName().trim()).setOrderNum(vo.getOrderNum()).setParentId(vo.getParentId()).setPath(path);
        this.save(entity);
    }

    private String validParent(Long parentId) {
        if (parentId != null) {
            OrganizationEntity entity = this.getById(parentId);
            CommonError.NOT_EXISTS.notNull(entity, "上级组织机构");
            StringBuilder path = new StringBuilder();
            if (CharSequenceUtil.isNotBlank(entity.getPath())) {
                path.append(entity.getPath());
            }
            return path.append(parentId).append("/").toString();
        }
        return null;
    }

    @Override
    public void update(OrganizationVO vo) {
        CommonError.NOT_NULL.notNull(vo.getId(), "id");

        LambdaQueryWrapper<OrganizationEntity> query = new LambdaQueryWrapper<OrganizationEntity>().eq(OrganizationEntity::getName, vo.getName().trim());
        OrganizationEntity entity = this.getOne(query);
        OrganizationError.ORGANIZATION_NAME_EXIST.isTrue(entity == null || vo.getId().equals(entity.getId()));

        entity = getById(vo.getId());
        OrganizationError.NOT_FIND_ORGANIZATION.notNull(entity);

        //不能变更组织上级组织机构
        OrganizationError.NOT_UPDATE_PARENT_ORG.isTrue(CharSequenceUtil.equals(String.valueOf(vo.getParentId()), String.valueOf(entity.getParentId())));

        String path = this.validParent(vo.getParentId());

        entity.setName(vo.getName().trim()).setOrderNum(vo.getOrderNum()).setPath(path);
        this.updateById(entity);
    }

    /**
     * 根据id删除组织信息
     *
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        //存在子节点不能删除
        long count = this.count(new LambdaQueryWrapper<OrganizationEntity>().eq(OrganizationEntity::getParentId, id));
        OrganizationError.CHILD_NODE_EXISTS.isTrue(count == 0);

        //存在用户不能删除
        count = this.userMapper.selectCount(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getOrgId, id));
        OrganizationError.USER_EXISTS.isTrue(count == 0);

        this.removeById(id);
    }

    /**
     * 组织机构树
     *
     * @param containsUser 是否包含用户
     * @return
     */
    @Override
    public List<OrganizationTreeVO> tree(boolean containsUser) {
        List<OrganizationTreeVO> list = CollUtil.newArrayList();
        List<OrganizationEntity> orgList = this.baseMapper.selectList(new LambdaQueryWrapper<OrganizationEntity>().orderByAsc(OrganizationEntity::getOrderNum));
        if (CollUtil.isEmpty(orgList)) {
            return list;
        }

        Map<Long, List<OrganizationTreeVO>> userMap = new HashMap<>();
        if (containsUser) {
            //获取所有用户
            userMap.putAll(this.getUserMap());
        }

        //所有顶级机构
        List<OrganizationTreeVO> parentList = orgList.stream().filter(org -> org.getParentId() == null)
                .map(this::convertOrg)
                .collect(Collectors.toList());

        //根据上级组织机构id分类
        Map<Long, List<OrganizationTreeVO>> orgMap = orgList.stream().filter(org -> org.getParentId() != null).collect(Collectors.toMap(OrganizationEntity::getParentId,
                org -> ListUtil.toList(this.convertOrg(org)), (oldList, newList) -> (List<OrganizationTreeVO>) CollUtil.addAll(oldList, newList)));

        parentList.stream().forEach(parent -> {
            list.add(parent);
            this.orgTree(parent, orgMap.get(parent.getId()), orgMap, userMap);
            this.addUser(parent, userMap);
        });
        return list;
    }

    private void orgTree(OrganizationTreeVO parent, List<OrganizationTreeVO> list, Map<Long, List<OrganizationTreeVO>> orgMap, Map<Long, List<OrganizationTreeVO>> userMap) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        list.stream().forEach(menu -> {
            parent.getChildren().add(menu);
            this.addUser(menu, userMap);
            this.orgTree(menu, orgMap.get(menu.getId()), orgMap, userMap);
        });
    }

    private void addUser(OrganizationTreeVO parent, Map<Long, List<OrganizationTreeVO>> userMap) {
        if (userMap.containsKey(parent.getId())) {
            //添加组织机构下存在的用户
            parent.getChildren().addAll(userMap.get(parent.getId()));
        }
    }

    /**
     * 获取所有用户
     *
     * @return key 组织机构id
     */
    private Map<Long, List<OrganizationTreeVO>> getUserMap() {
        List<UserEntity> userList = this.userMapper.selectList(new LambdaQueryWrapper<UserEntity>().ne(UserEntity::getStatus, UserStatusEnum.CANCEL));
        if (CollUtil.isEmpty(userList)) {
            return MapUtil.newHashMap();
        }
        return userList.stream().collect(Collectors.toMap(UserEntity::getOrgId,
                user -> ListUtil.toList(this.convertUser(user)), (oldList, newList) -> (List<OrganizationTreeVO>) CollUtil.addAll(oldList, newList)));
    }

    private OrganizationTreeVO convertUser(UserEntity entity) {
        return new OrganizationTreeVO()
                .setTreeId(String.format("%s_%s",entity.getId(), OrgTreeTypeEnum.USER.name()))
                .setId(entity.getId())
                .setName(entity.getRealName())
                .setParentId(entity.getOrgId())
                .setUsername(entity.getUsername())
                .setUserStatus(entity.getStatus())
                .setType(OrgTreeTypeEnum.USER);
    }

    private OrganizationTreeVO convertOrg(OrganizationEntity entity) {
        return new OrganizationTreeVO()
                .setTreeId(String.format("%s_%s",entity.getId(), OrgTreeTypeEnum.ORG.name()))
                .setId(entity.getId())
                .setParentId(entity.getParentId())
                .setName(entity.getName())
                .setOrderNum(entity.getOrderNum())
                .setType(OrgTreeTypeEnum.ORG);
    }
}
