package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import exam.common.enums.LogLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ssm.common.log.LogType;


/**
 * <p>
 * 日志表
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("log")
@Accessors(chain = true)
public class LogEntity extends BaseEntity {

    private LogType type;

    private String appName;

    private String sysName;

    private String ip;

    private LogLevel level;

    private String code;

    private String message;

    private String threadName;

    private boolean warning;

    private String action;

    private String traceId;

    private String subType;

    private String subTypeValue;

    private String userId;

    private String username;

    private LocalDateTime generateTime;

}
