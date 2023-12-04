package exam.bus;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.vo.QuestionCategoryAddVO;
import exam.common.vo.QuestionCategoryTreeVO;
import exam.common.vo.StatusUpdateVO;
import exam.common.vo.QuestionCategoryUpdateVO;
import exam.dao.entity.QuestionCategoryEntity;
import ssm.common.entity.ComboBox;

import java.util.List;
import java.util.Set;

/**
 * @author zhoufs
 * @since 2022-02-16 10:30
 */
public interface IQuestionCategoryService extends IService<QuestionCategoryEntity> {

    /**
     * 新增问题类目
     *
     * @param vo
     */
    void add(QuestionCategoryAddVO vo);

    /**
     * 修改问题类目
     *
     * @param vo
     */
    void update(QuestionCategoryUpdateVO vo);

    /**
     * 修改问题类目状态
     *
     * @param vo
     */
    String changeStatus(StatusUpdateVO vo);

    /**
     * 删除问题类目
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 批量删除问题类目
     *
     * @param ids
     */
    void delete(Set<Long> ids);

    /**
     * 查询已启用的问题类目
     *
     * @return
     */
    List<ComboBox> comboBoxEnableCategory();

    /**
     * 获取所有考题类目
     *
     * @return
     */
    List<QuestionCategoryUpdateVO> getCategoryList();

    /**
     * 条件分页查询问题类目
     *
     * @param vo
     * @param page
     * @return
     */
    IPage<QuestionCategoryUpdateVO> page(QuestionCategoryAddVO vo, Page page);

    List<QuestionCategoryTreeVO> tree();

    List<QuestionCategoryEntity> queryAllCategory();
}
