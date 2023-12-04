package exam.bus;

import com.baomidou.mybatisplus.extension.service.IService;
import exam.dao.entity.PracticeQuestionEntity;

import java.util.List;

/**
 * @version 1.0.0
 * @date: 2022/7/13 15:01
 * @author: yangbo
 */
public interface IPracticeQuestionService extends IService<PracticeQuestionEntity> {

    List<PracticeQuestionEntity> findByPracticeId(Long practiceId);
}
