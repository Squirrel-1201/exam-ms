package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

@AllArgsConstructor
@Getter
public enum MenuTypeEnum implements BaseEnum {

    DIRECTORY(0, "目录"),
    MENU(1, "菜单"),
    ROUTE(2, "路由");

    @EnumValue
    private int code;

    private String desc;
}
