package exam.bus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.common.vo.ExamStatisticeQueryVO;
import exam.common.vo.ExamStatisticeTotalVO;
import exam.common.vo.ScoreQueryVO;
import exam.common.vo.ScoreVO;
import exam.common.vo.StuScoreVO;

public interface IScoreService {

    /**
     * 成绩管理-分页
     *
     * @param page
     * @param vo
     * @return
     */
    IPage<ScoreVO> pageList(Page page, ScoreQueryVO vo);

    /**
     * 考试通过详情
     *
     * @param examId
     * @return
     */
    IPage<StuScoreVO> pass( Page page,Long examId);

    /**
     * 考试未通过详情
     *
     * @param examId
     * @return
     */
    IPage<StuScoreVO> notPass( Page page,Long examId);

    IPage<ExamStatisticeTotalVO> statisticsExam(ExamStatisticeQueryVO vo, Page page);
}
