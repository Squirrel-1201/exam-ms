package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

@AllArgsConstructor
@Getter
public enum YNEnum implements BaseEnum {

    NO(0, "是"),
    YES(1, "否");

    @EnumValue
    private int code;

    private String desc;
}
