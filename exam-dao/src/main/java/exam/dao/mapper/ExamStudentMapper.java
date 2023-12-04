package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.common.vo.StuScoreVO;
import exam.common.vo.StudentExamVO;
import exam.dao.entity.ExamStudentEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 试卷和考生的关联表 Mapper 接口
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
public interface ExamStudentMapper extends BaseMapper<ExamStudentEntity> {

    IPage<StudentExamVO> planList(@Param("vo") StudentExamVO vo, Page page);

    IPage<StudentExamVO> endList(@Param("vo") StudentExamVO vo, Page page);

    IPage<StuScoreVO> findByExamIdAndStatus(@Param("status") Set<Integer> status, @Param("examId") Long examId, Page page);

    Set<Long> findStuIdByExamId(@Param("examId") Long examId);
}
