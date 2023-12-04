package exam.bus.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IQuestionCategoryService;
import exam.common.enums.YNEnum;
import exam.common.vo.QuestionCategoryTreeVO;
import exam.common.vo.QuestionCountVO;
import exam.common.vo.StatusUpdateVO;
import exam.common.enums.EnableEnum;
import exam.common.errorcode.CommonError;
import exam.common.errorcode.QuestionCategoryErrorCode;
import exam.common.vo.QuestionCategoryAddVO;
import exam.common.vo.QuestionCategoryUpdateVO;
import exam.common.errorcode.QuestionErrorCode;
import exam.dao.entity.QuestionCategoryEntity;
import exam.dao.entity.QuestionEntity;
import exam.dao.mapper.QuestionCategoryMapper;
import exam.dao.mapper.QuestionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssm.common.entity.BaseEntity;
import ssm.common.entity.ComboBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/16 10:36
 */
@Service
@AllArgsConstructor
@Slf4j
public class QuestionCategoryServiceImpl extends ServiceImpl<QuestionCategoryMapper, QuestionCategoryEntity> implements IQuestionCategoryService {

    private QuestionMapper questionMapper;

    @Override
    public void add(QuestionCategoryAddVO vo) {
        // 校验试题类别名称
        QuestionCategoryEntity category = this.getOne(new LambdaQueryWrapper<QuestionCategoryEntity>().eq(QuestionCategoryEntity::getName, vo.getName()));
        QuestionCategoryErrorCode.CATEGORY_NAME_HAD_EXIST.isNull(category);

        QuestionCategoryEntity entity = new QuestionCategoryEntity();
        BeanUtils.copyProperties(vo, entity);
        this.save(entity);
    }

    @Override
    public void update(QuestionCategoryUpdateVO vo) {
        CommonError.NOT_NULL.notNull(vo.getId(), "id");
        QuestionCategoryErrorCode.NOT_ALLOW_ID_EQUAL_PARENTID.isFalse(vo.getId().equals(vo.getParentId()));

        QuestionCategoryEntity entity = this.getById(vo.getId());
        QuestionCategoryErrorCode.NOT_FIND_QUESTION_CATEGORY.notNull(entity);
        // 根节点不允许被修改
        if (entity.getParentId() == -1) {
            QuestionCategoryErrorCode.NOT_ALLOW_MODIFY_ROOT.assertThrow();
        }
        if (!vo.getParentId().equals(entity.getParentId())){
            long count = questionMapper.selectCount(new LambdaQueryWrapper<QuestionEntity>().eq(QuestionEntity::getQuestionCategoryId, vo.getId()));
            QuestionCategoryErrorCode.NOT_ALLOW_MODIFY_CONTAINS_QUESTION.isTrue(count < 1);
        }
        QuestionCategoryEntity category = this.getOne(new LambdaQueryWrapper<QuestionCategoryEntity>().eq(QuestionCategoryEntity::getName, vo.getName()).ne(BaseEntity::getId, vo.getId()));
        QuestionCategoryErrorCode.CATEGORY_NAME_HAD_EXIST.isNull(category);

        QuestionCategoryEntity questionCategoryEntity = new QuestionCategoryEntity();
        BeanUtils.copyProperties(vo, questionCategoryEntity);
        this.updateById(questionCategoryEntity);
    }

    @Override
    public String changeStatus(StatusUpdateVO vo) {
        QuestionCategoryEntity entity = getById(vo.getId());
        QuestionErrorCode.NOT_FIND_QUESTION_CATEGORY.notNull(entity);
        entity.setStatus(vo.getStatus());
        this.updateById(entity);
        return entity.getName();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        QuestionCategoryErrorCode.NOT_ALLOW_MODIFY_ROOT.isFalse(1 == id);
        // 判断该类目下是否已有考题
        long count = this.questionMapper.selectCount(new LambdaQueryWrapper<QuestionEntity>().eq(QuestionEntity::getQuestionCategoryId, id));
        QuestionCategoryErrorCode.CATEGORY_EXIST_QUESTION.isTrue(count < 1);
        long childNodes = baseMapper.selectCount(new LambdaQueryWrapper<QuestionCategoryEntity>().eq(QuestionCategoryEntity::getParentId, id));
        QuestionCategoryErrorCode.CATEGORY_EXIST_CHILD_NODE.isTrue(childNodes < 1);
        this.removeById(id);
    }

    @Override
    @Transactional
    public void delete(Set<Long> ids) {
        CommonError.NOT_NULL.notEmpty(ids, "id");
        QuestionCategoryErrorCode.NOT_ALLOW_MODIFY_ROOT.isFalse(ids.contains(1L));
        // 判断该类目下是否已有考题
        long count = this.questionMapper.selectCount(new LambdaQueryWrapper<QuestionEntity>().in(QuestionEntity::getQuestionCategoryId, ids));
        QuestionCategoryErrorCode.CATEGORY_EXIST_QUESTION.isTrue(count < 1);
        long childNodes = baseMapper.selectCount(new LambdaQueryWrapper<QuestionCategoryEntity>().in(QuestionCategoryEntity::getParentId, ids));
        QuestionCategoryErrorCode.CATEGORY_EXIST_CHILD_NODE.isTrue(childNodes < 1);
        this.removeByIds(ids);
    }

    @Override
    public List<ComboBox> comboBoxEnableCategory() {
        List<QuestionCategoryEntity> list = this.list(new LambdaQueryWrapper<QuestionCategoryEntity>()
                .eq(QuestionCategoryEntity::getStatus, EnableEnum.ENABLE));

        return Optional.ofNullable(list).orElse(CollUtil.newArrayList())
                .stream()
                .map(entity -> new ComboBox().setValue(String.valueOf(entity.getId())).setLabel(entity.getName()))
                .collect(Collectors.toList());

    }

    @Override
    public List<QuestionCategoryUpdateVO> getCategoryList() {
        return this.baseMapper.getCategoryList();
    }

    @Override
    public IPage<QuestionCategoryUpdateVO> page(QuestionCategoryAddVO vo, Page page) {
        LambdaQueryWrapper<QuestionCategoryEntity> wrapper = new LambdaQueryWrapper<>();
        if (vo != null) {
            wrapper.eq(vo.getStatus() != null, QuestionCategoryEntity::getStatus, vo.getStatus())
                    .like(CharSequenceUtil.isNotBlank(vo.getName()), QuestionCategoryEntity::getName, vo.getName());
        }
        wrapper.orderByDesc(BaseEntity::getUpdateTime);

        IPage<QuestionCategoryEntity> pageList = this.page(page, wrapper);
        IPage<QuestionCategoryUpdateVO> iPage = new Page<>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal());

        if (CollUtil.isEmpty(pageList.getRecords())) {
            return iPage;
        }
        List<QuestionCategoryUpdateVO> list = pageList.getRecords().stream().map(entity -> {
            QuestionCategoryUpdateVO q = new QuestionCategoryUpdateVO();
            BeanUtil.copyProperties(entity, q);
            return q;
        }).collect(Collectors.toList());

        return iPage.setRecords(list);
    }

    @Override
    public List<QuestionCategoryTreeVO> tree() {
        List<QuestionCategoryTreeVO> result = new ArrayList<>();
        List<QuestionCategoryEntity> entityList = baseMapper.selectList(new LambdaQueryWrapper<QuestionCategoryEntity>().eq(QuestionCategoryEntity::getStatus, EnableEnum.ENABLE));
        List<QuestionCountVO> questionCountVOS = questionMapper.countByCategoryAndType();
        Map<Long, List<QuestionCountVO>> questionCountMap = questionCountVOS.stream().collect(Collectors.groupingBy(QuestionCountVO::getId));

        List<QuestionCategoryTreeVO> collect = entityList.stream().map(this::convertToTreeObj).collect(Collectors.toList());
        Map<Long, List<QuestionCategoryTreeVO>> categoryMap = collect.stream().collect(Collectors.groupingBy(QuestionCategoryTreeVO::getParentId));

        QuestionCategoryTreeVO root = collect.stream().filter(t -> t.getParentId() == -1).findFirst().orElse(null);
        QuestionCategoryErrorCode.NOT_FIND_ROOT.notNull(root);
        treeChildNode(root, categoryMap, questionCountMap);
        computeQuestionCount(root);
        result.add(root);
        return result;
    }

    @Override
    public List<QuestionCategoryEntity> queryAllCategory() {
        return baseMapper.selectList(new LambdaQueryWrapper<>());
    }

    /**
     * 统计所有分类下的题目数量
     *
     * @param node
     * @return
     */
    private Integer computeQuestionCount(QuestionCategoryTreeVO node) {
        if (node.getLeaf() == YNEnum.YES) {
            return node.getCount();
        }
        Integer result = 0;
        List<QuestionCategoryTreeVO> childList = node.getChild();
        for (QuestionCategoryTreeVO child : childList) {
            result = result + computeQuestionCount(child);
        }
        node.setCount(result);
        return result;
    }

    /**
     * 查找所有叶子节点，并添加当前节点的
     *
     * @param parentVO
     * @param categoryMap
     * @param questionCountMap
     */
    private void treeChildNode(QuestionCategoryTreeVO parentVO, Map<Long, List<QuestionCategoryTreeVO>> categoryMap, Map<Long, List<QuestionCountVO>> questionCountMap) {
        List<QuestionCategoryTreeVO> childNodeList = categoryMap.get(parentVO.getId());
        if (CollUtil.isEmpty(childNodeList)) {
            return;
        }
        childNodeList.forEach(node -> {
            parentVO.getChild().add(node);
            treeChildNode(node, categoryMap, questionCountMap);
            addExamType(node, questionCountMap);
        });

    }

    private void addExamType(QuestionCategoryTreeVO node, Map<Long, List<QuestionCountVO>> questionCountMap) {
        List<QuestionCountVO> questionCountVOS = questionCountMap.get(node.getId());
        if (CollUtil.isEmpty(questionCountVOS)) {
            return;
        }
        questionCountVOS.forEach(t -> node.getChild().add(convertCountVOToObj(t, node)));
    }

    private QuestionCategoryTreeVO convertCountVOToObj(QuestionCountVO vo, QuestionCategoryTreeVO parent) {
        return new QuestionCategoryTreeVO()
                .setId(vo.getId())
                .setTreeId(String.format("%s_%s", vo.getId(), vo.getType().getCode()))
                .setLeaf(YNEnum.YES)
                .setCount(vo.getCount())
                .setName(vo.getType().getDesc())
                .setStatus(parent.getStatus())
                .setType(vo.getType().getDesc())
                .setParentId(parent.getParentId());
    }

    private QuestionCategoryTreeVO convertToTreeObj(QuestionCategoryEntity entity) {
        return new QuestionCategoryTreeVO()
                .setId(entity.getId())
                .setTreeId(String.valueOf(entity.getId()))
                .setName(entity.getName())
                .setParentId(entity.getParentId())
                .setStatus(entity.getStatus())
                .setLeaf(YNEnum.NO);
    }

}
