package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.common.vo.FinishPracticePaperVO;
import exam.common.vo.PracticePaperQueryVO;
import exam.common.vo.PracticedPaperVO;
import exam.dao.entity.PracticePaperEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @version 1.0.0
 * @date: 2022/7/13 15:52
 * @author: yangbo
 */
public interface PracticePaperMapper extends BaseMapper<PracticePaperEntity> {

    Page<PracticedPaperVO> findPracticedList(@Param("vo") PracticePaperQueryVO vo, Page page);

    IPage<FinishPracticePaperVO> queryFinishPracticeList(@Param("vo") FinishPracticePaperVO vo, Page page);
}
