package exam.bus;

import com.baomidou.mybatisplus.extension.service.IService;
import exam.dao.entity.ExamRecordEntity;

public interface IExamRecordService extends IService<ExamRecordEntity> {



    /**
     * 生成指定试卷的所有考生考题
     *
     * @param examId 试卷id
     */
    void generateStuExam(final Long examId);



    /**
     * 生成指定考生试卷
     *
     * @param examId 试卷id
     * @param userId 考试id
     */
    void generateStuExam(Long examId, Long userId);

}
