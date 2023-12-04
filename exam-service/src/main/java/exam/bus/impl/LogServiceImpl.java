package exam.bus.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.ILogService;
import exam.bus.ILogSetService;
import exam.common.enums.LogLevel;
import exam.common.vo.LogSetVO;
import exam.common.vo.LogVO;
import exam.dao.entity.LogEntity;
import exam.dao.mapper.LogMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssm.common.log.BaseLogVo;
import ssm.common.log.biz.BizLogVo;
import ssm.common.log.login.LoginLogVo;
import ssm.common.log.oper.OperLogVo;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogEntity> implements ILogService {

    private ILogSetService logSetService;

    @Override
    public void save(BaseLogVo logVo) {
        LogSetVO setVO = this.logSetService.detail();
        LogLevel logLevel = LogLevel.valueOf(logVo.getLevel());
        if (logLevel.getCode() < setVO.getLevel().getCode()) {
            return;
        }

        LogEntity entity = new LogEntity()
                .setType(logVo.getLogType())
                .setIp(logVo.getIp())
                .setAppName(logVo.getAppName())
                .setSysName(logVo.getSysName())
                .setLevel(LogLevel.valueOf(logVo.getLevel()))
                .setGenerateTime(LocalDateTimeUtil.of(logVo.getCreateTime()))
                .setCode(logVo.getCode())
                .setMessage(logVo.getMessage())
                .setThreadName(logVo.getThreadName())
                .setWarning(logVo.isWarning());

        switch (logVo.getLogType()) {
            case LOGIN:
                LoginLogVo login = (LoginLogVo) logVo;
                entity.setAction(login.getAction()).setTraceId(login.getTraceId()).setSubType(login.getSubType())
                        .setSubTypeValue(login.getSubTypeValue()).setUserId(login.getUserId()).setUsername(login.getUserName());
                break;
            case BIZ:
                BizLogVo bizLog = (BizLogVo) logVo;
                entity.setAction(bizLog.getAction()).setTraceId(bizLog.getTraceId()).setSubType(bizLog.getSubType())
                        .setSubTypeValue(bizLog.getSubTypeValue()).setUserId(bizLog.getUserId()).setUsername(bizLog.getUserName()).setMessage((String) bizLog.getDetail());
                break;
            case OPER:
                OperLogVo operLog = (OperLogVo) logVo;
                entity.setAction(operLog.getAction()).setTraceId(operLog.getTraceId()).setSubType(operLog.getSubType())
                        .setSubTypeValue(operLog.getSubTypeValue()).setUserId(operLog.getUserId()).setUsername(operLog.getUserName()).setMessage((String) operLog.getDetail());
                break;
            default:
                break;
        }
        entity.setCreateBy(entity.getUsername()).setUpdateBy(entity.getUsername());
        this.save(entity);
    }

    @Override
    public IPage<LogVO> pageList(Page page, LogVO vo) {
        LambdaQueryWrapper<LogEntity> wrapper = new LambdaQueryWrapper<>();
        if (vo != null) {
            wrapper.like(CharSequenceUtil.isNotBlank(vo.getIp()), LogEntity::getIp, vo.getIp())
                    .like(CharSequenceUtil.isNotBlank(vo.getUsername()), LogEntity::getUsername, vo.getUsername())
                    .like(CharSequenceUtil.isNotBlank(vo.getAction()), LogEntity::getAction, vo.getAction());
        }
        wrapper.orderByDesc(LogEntity::getGenerateTime);
        Page<LogEntity> pageList = this.page(page, wrapper);
        IPage<LogVO> iPage = new Page<>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal());
        if (CollUtil.isEmpty(pageList.getRecords())) {
            return iPage;
        }
        List<LogVO> list = pageList.getRecords().stream().map(this::convert).collect(Collectors.toList());
        return iPage.setRecords(list);
    }

    private LogVO convert(LogEntity entity) {
        LogVO vo = new LogVO();
        BeanUtil.copyProperties(entity, vo);
        vo.setDetail(entity.getMessage());
        return vo;
    }

    @Override
    public void autoClearLog() {
        log.info("自动清除日志开始");
        LogSetVO vo = this.logSetService.detail();
        log.info("自动清除超过{}天的日志", vo.getSaveDay());
        if (vo.getSaveDay() != null) {
            this.remove(new LambdaQueryWrapper<LogEntity>().le(LogEntity::getGenerateTime, LocalDateTimeUtil.now().minusDays(vo.getSaveDay())));
        }
        log.info("自动清除日志结束");
    }
}
