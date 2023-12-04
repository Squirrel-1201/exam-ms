package exam.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.common.vo.ArtificialMarkExamResultVO;
import exam.common.vo.ArtificialMarkQueryVO;
import exam.common.vo.RecentExamResultVO;
import exam.common.vo.StuExamQueryVO;
import exam.dao.entity.ExamEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 试卷表 Mapper 接口
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
public interface ExamMapper extends BaseMapper<ExamEntity> {

    Page<ArtificialMarkExamResultVO> getMarkingExamList(@Param("vo") ArtificialMarkQueryVO vo, Page page);

    Page<ArtificialMarkExamResultVO> getStudentExamList(@Param("vo") StuExamQueryVO vo, Page page);

    List<RecentExamResultVO> recentExamStatistics();
}
