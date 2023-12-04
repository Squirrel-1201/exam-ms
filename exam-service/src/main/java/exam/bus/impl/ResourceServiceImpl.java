package exam.bus.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IResourceService;
import exam.common.vo.ResourceVO;
import exam.common.constant.Constant;
import exam.common.errorcode.CommonError;
import exam.dao.entity.ResourceEntity;
import exam.common.enums.YNEnum;
import exam.dao.mapper.ResourceMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssm.common.entity.BaseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, ResourceEntity> implements IResourceService {

    @Override
    public IPage<ResourceVO> pageList(ResourceVO vo, Page page) {
        LambdaQueryWrapper<ResourceEntity> wrapper = new LambdaQueryWrapper<>();
        if (vo != null) {
            wrapper.like(CharSequenceUtil.isNotBlank(vo.getResourceName()), ResourceEntity::getResourceName, vo.getResourceName());
            wrapper.eq(Objects.nonNull(vo.getLoginVerify()), ResourceEntity::getLoginVerify, vo.getLoginVerify());
        }

        wrapper.orderByDesc(BaseEntity::getUpdateTime);
        IPage<ResourceEntity> iPage = this.page(page, wrapper);
        List<ResourceVO> list = this.convert(iPage.getRecords());
        return new Page<ResourceVO>(iPage.getCurrent(), iPage.getSize(), iPage.getTotal()).setPages(iPage.getPages()).setRecords(list);
    }


    private List<ResourceVO> convert(List<ResourceEntity> list) {
        return Optional.ofNullable(list).orElse(CollUtil.newArrayList())
                .stream().map(resource -> {
                    ResourceVO resourceVO = new ResourceVO();
                    BeanUtil.copyProperties(resource, resourceVO);
                    return resourceVO;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = {Constant.LOGIN_RESOURCE, Constant.USER_PERM,Constant.USER_ROLE}, allEntries = true)
    public void add(ResourceVO vo) {
        ResourceEntity resource = new ResourceEntity();
        BeanUtil.copyProperties(vo, resource);
        this.save(resource);
    }

    @Override
    @Transactional
    @CacheEvict(value = {Constant.LOGIN_RESOURCE, Constant.USER_PERM,Constant.USER_ROLE}, allEntries = true)
    public void update(ResourceVO vo) {
        CommonError.NOT_NULL.notNull(vo.getId(), "id");
        ResourceEntity resource = new ResourceEntity();
        BeanUtil.copyProperties(vo, resource);
        this.updateById(resource);
    }

    @Override
    @Transactional
    @CacheEvict(value = {Constant.LOGIN_RESOURCE, Constant.USER_PERM,Constant.USER_ROLE}, allEntries = true)
    public void delete(Set<Long> ids) {
        CommonError.NOT_NULL.notEmpty(ids, "id");
        this.removeByIds(ids);
    }

    @Override
    public List<ResourceVO> findByMenuId(Long menuId) {
        List<ResourceEntity> list = this.baseMapper.findByMenuId(menuId);
        return this.convert(list);
    }

    @Override
    @Cacheable(value = Constant.LOGIN_RESOURCE, unless = "#result == null ")
    public List<String> getLoginResource() {
        LambdaQueryWrapper<ResourceEntity> wrapper = new LambdaQueryWrapper<ResourceEntity>()
                .eq(ResourceEntity::getLoginVerify, YNEnum.YES)
                .isNotNull(ResourceEntity::getUrl)
                .select(ResourceEntity::getUrl);
        return this.listObjs(wrapper, Object::toString);
    }
}
