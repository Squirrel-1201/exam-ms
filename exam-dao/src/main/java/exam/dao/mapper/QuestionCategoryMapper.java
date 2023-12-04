package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import exam.common.vo.QuestionCategoryUpdateVO;
import exam.dao.entity.QuestionCategoryEntity;

import java.util.List;

/**
 * <p>
 * 问题类目 Mapper 接口
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
public interface QuestionCategoryMapper extends BaseMapper<QuestionCategoryEntity> {

    List<QuestionCategoryUpdateVO> getCategoryList();
}
