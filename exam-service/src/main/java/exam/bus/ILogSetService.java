package exam.bus;

import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.vo.LogSetVO;
import exam.dao.entity.LogSetEntity;

public interface ILogSetService extends IService<LogSetEntity> {

    LogSetVO detail();

    void update(LogSetVO vo);
}
