package exam.bus;

import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.vo.PracticeStudentAddVO;
import exam.dao.entity.PracticeStudentEntity;

import java.util.List;

/**
 * @version 1.0.0
 * @date: 2022/7/13 17:46
 * @author: yangbo
 */
public interface IPracticeStudentService extends IService<PracticeStudentEntity> {
    /**
     * 添加参加练习的考生
     *
     * @param vo
     */
    String addPracticeStudent(PracticeStudentAddVO vo);

    /**
     * 根据练习id查看考生id集合
     *
     * @param practiceId
     * @return
     */
    List<Long> findStudentsByPracticeId(Long practiceId);
}
