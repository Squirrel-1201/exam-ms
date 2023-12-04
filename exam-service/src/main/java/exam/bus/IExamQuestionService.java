package exam.bus;

import com.baomidou.mybatisplus.extension.service.IService;
import exam.dao.entity.ExamQuestionEntity;

import java.util.List;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/18 10:49
 */
public interface IExamQuestionService extends IService<ExamQuestionEntity> {

    /**
     * 根据试卷id查询试卷题目集合
     * @param examId
     * @return
     */
    List<ExamQuestionEntity> findExamQuestionByExamId(Long examId);
}
