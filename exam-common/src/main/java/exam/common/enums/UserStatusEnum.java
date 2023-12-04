package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

@AllArgsConstructor
@Getter
public enum UserStatusEnum implements BaseEnum {

    DISABLE(0, "禁用"),
    ENABLE(1, "启用"),
    LOCK(2, "锁定"),
    CANCEL(3, "注销");

    @EnumValue
    private int code;

    private String desc;
}
