package exam.bus.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.ILogSetService;
import exam.common.enums.LogLevel;
import exam.common.vo.LogSetVO;
import exam.dao.entity.LogSetEntity;
import exam.dao.mapper.LogSetMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@AllArgsConstructor
@Service
@Slf4j
public class LogSetServiceImpl extends ServiceImpl<LogSetMapper, LogSetEntity> implements ILogSetService {

    private static final Long ID = 0L;

    @Override
    public LogSetVO detail() {
        LogSetEntity entity = this.getById(ID);
        if (entity == null) {
            return new LogSetVO().setLevel(LogLevel.INFO).setSaveDay(365);
        }
        return new LogSetVO().setLevel(entity.getLevel()).setSaveDay(entity.getSaveDay());
    }

    @Override
    @Transactional
    public void update(LogSetVO vo) {
        LogSetEntity entity = this.getById(ID);
        LogSetEntity logSet = (LogSetEntity) new LogSetEntity().setLevel(vo.getLevel()).setSaveDay(vo.getSaveDay()).setId(ID);
        if (entity == null) {
            this.save(logSet);
        } else {
            this.updateById(logSet);
        }
    }
}
