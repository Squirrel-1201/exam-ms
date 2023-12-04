package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

@AllArgsConstructor
@Getter
public enum LogLevel implements BaseEnum {

    TRACE(0, "追踪"),
    DEBUG(1, "调试"),
    INFO(2, "一般"),
    WARN(3, "警告"),
    ERROR(4, "错误"),
    ;

    @EnumValue
    private int code;

    private String desc;

}
