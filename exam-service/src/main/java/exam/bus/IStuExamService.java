package exam.bus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.common.vo.StudentExamDetailVO;
import exam.common.vo.StudentExamVO;
import exam.common.vo.SubmitAnswerVO;
import exam.common.vo.SubmitExamVO;

public interface IStuExamService {

    /**
     * 考试计划列表
     *
     * @param vo
     * @param page
     * @return
     */
    IPage<StudentExamVO> planList(StudentExamVO vo, Page page);


    /**
     * 已结束的考试列表
     *
     * @param vo
     * @param page
     * @return
     */
    IPage<StudentExamVO> endList(StudentExamVO vo, Page page);

    /**
     * 开始考试
     *
     * @param examId 试卷id
     * @param userId 用户id
     * @return
     */
    StudentExamDetailVO startExam(String ip, Long examId, Long userId);

    /**
     * 继续考试
     *
     * @param examId 试卷id
     * @param userId 用户id
     * @return
     */
    StudentExamDetailVO continueExam(String ip, Long examId, Long userId);

    /**
     * 考试结果
     *
     * @param examId 试卷id
     * @param userId 用户id
     * @return
     */
    StudentExamDetailVO examResult(Long examId, Long userId);

    /**
     * 提交问题答案
     *
     * @param vo
     */
    void submitAnswer(SubmitAnswerVO vo);

    /**
     * 交卷
     *
     * @param vo
     */
    String submitExam(SubmitExamVO vo);

    /**
     * 自动交卷
     */
    void autoSubmitExam();

    /**
     * 自动阅卷
     */
    void autoSubmitMarking();

    /**
     * 自动结束考试
     */
    void autoEndExam();

}
