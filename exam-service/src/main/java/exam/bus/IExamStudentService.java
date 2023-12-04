package exam.bus;

import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.vo.ExamStudentAddVO;
import exam.dao.entity.ExamStudentEntity;

import java.util.List;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/25 14:04
 */
public interface IExamStudentService extends IService<ExamStudentEntity> {

    /**
     * 添加参加考试的考生
     * @param vo
     */
    String addExamStudent(ExamStudentAddVO vo);

    /**
     * 根据试卷id查看考生id集合
     * @param examId
     * @return
     */
    List<Long> findStudentsByExamId(Long examId);
}
