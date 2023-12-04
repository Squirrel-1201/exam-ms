package exam.common.enums.subtype;

import lombok.AllArgsConstructor;
import ssm.common.log.SubType;

@AllArgsConstructor
public enum LoginSubTypeEnum implements SubType {

    LOGIN("10000", "用户登录"),
    LOGOUT("10001", "用户登出");
    private String code;

    private String value;


    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String value() {
        return this.value;
    }
}
