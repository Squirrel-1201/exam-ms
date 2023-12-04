package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import exam.common.vo.QuestionCountByCategory;
import exam.common.vo.QuestionCountVO;
import exam.common.vo.QuestionQueryVO;
import exam.common.vo.QuestionResultVO;
import exam.dao.entity.QuestionEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 题库 Mapper 接口
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
public interface QuestionMapper extends BaseMapper<QuestionEntity> {

    @Select("select count(id) num from question where question_category_id=#{questionCategoryId} and status=1 and type=#{type} and difficulty=#{difficulty}")
    Integer countQuestionNumByDifficultAndType(@Param("questionCategoryId") Long questionCategoryId, @Param("type") QuestionTypeEnum type, @Param("difficulty")QuestionDifficultyEnum difficulty);

    List<QuestionEntity> findByExamIdAndStuId(@Param("examId") Long examId, @Param("stuId") Long stuId);

    Page<QuestionResultVO> pageList(@Param("vo") QuestionQueryVO vo, Page page);

    List<QuestionCountVO> countByCategoryAndType();

    Page<QuestionResultVO> duplicateListQuery(@Param("vo") QuestionQueryVO vo, Page page);

    Integer containsTitle(@Param("title") String title);

    List<QuestionCountByCategory> questionCountByCategory();

}
