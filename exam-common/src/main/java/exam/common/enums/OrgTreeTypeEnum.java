package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

@AllArgsConstructor
@Getter
public enum OrgTreeTypeEnum implements BaseEnum {

    ORG(0, "组织机构"),
    USER(1, "用户");

    @EnumValue
    private int code;

    private String desc;
}
