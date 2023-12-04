package exam.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import exam.dao.entity.ExamQuestionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 试卷题目设置表 Mapper 接口
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
public interface ExamQuestionMapper extends BaseMapper<ExamQuestionEntity> {
    List<ExamQuestionEntity> selectByExamId(@Param("examId") Long examId);

}
