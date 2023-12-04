package exam.bus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.vo.LogVO;
import exam.dao.entity.LogEntity;
import ssm.common.log.LogService;

public interface ILogService extends LogService, IService<LogEntity> {

    IPage<LogVO> pageList(Page page, LogVO vo);

    void autoClearLog();
}
