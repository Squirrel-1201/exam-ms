package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import exam.common.enums.LogLevel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 日志设置表
 */
@TableName("log_set")
@Data
@Accessors(chain = true)
public class LogSetEntity extends BaseEntity {

    private Integer saveDay;

    private LogLevel level;
}
