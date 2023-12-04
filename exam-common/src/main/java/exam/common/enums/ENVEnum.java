package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

@AllArgsConstructor
@Getter
public enum ENVEnum implements BaseEnum {

    DEV(0, "开发环境"),
    PRO(1, "生产环境");

    @EnumValue
    private int code;

    private String desc;
}
