package exam.bus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.common.vo.FinishPracticePaperVO;
import exam.common.vo.PracticeAnswerVO;
import exam.common.vo.PracticePaperDetailVO;
import exam.common.vo.StudentPracticeVO;
import exam.common.vo.SubmitPracticePaperVO;

/**
 * @version 1.0.0
 * @date: 2022/7/15 14:18
 * @author: yangbo
 */
public interface IStuPracticeService {

    IPage<StudentPracticeVO> practiceList(StudentPracticeVO vo, Page page);

    PracticePaperDetailVO startPractice(Long practiceId, Long userId);

    PracticePaperDetailVO continuePractice(Long practiceId, Long userId);

    PracticePaperDetailVO practicePaperResult(Long paperId);

    void submitAnswer(PracticeAnswerVO vo);

    String submitPracticePaper(SubmitPracticePaperVO vo);

    IPage<FinishPracticePaperVO> queryFinishPaperList(FinishPracticePaperVO vo, Page page);

    void autoEndPractice();

    void autoMarking();
}
