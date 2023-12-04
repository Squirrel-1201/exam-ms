package exam.dao.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.common.vo.ExamStatisticeQueryVO;
import exam.common.vo.ExamStatisticeTotalVO;
import exam.common.vo.StuExamQuestionVO;
import exam.dao.entity.ExamRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 考试记录 Mapper 接口
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
public interface ExamRecordMapper extends BaseMapper<ExamRecordEntity> {

    List<StuExamQuestionVO> getStuExamQuestion(@Param("userId") Long userId, @Param("examId") Long examId);

    void batchSave(@Param("list") List<ExamRecordEntity> list);

    @InterceptorIgnore(tenantLine = "true")
    IPage<ExamStatisticeTotalVO> statisticeTotal(@Param("vo") ExamStatisticeQueryVO vo, Page page);

}
