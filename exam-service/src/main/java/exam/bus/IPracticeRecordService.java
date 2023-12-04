package exam.bus;

import com.baomidou.mybatisplus.extension.service.IService;
import exam.dao.entity.PracticeRecordEntity;

/**
 * @version 1.0.0
 * @date: 2022/7/15 17:49
 * @author: yangbo
 */
public interface IPracticeRecordService extends IService<PracticeRecordEntity> {
    void generateStuPractice(Long practiceId, Long practicePaperId);
}
