package exam.common.enums.subtype;

import lombok.AllArgsConstructor;
import ssm.common.log.SubType;

@AllArgsConstructor
public enum SysSubTypeEnum implements SubType {

    ORG("30000", "组织机构管理"),
    USER("31000", "用户管理"),
    CATEGORY("32000", "问题类目管理"),
    QUESTION("33000", "问题管理"),
    EXAM("34000", "试卷管理"),
    PRACTICE("35000", "练习管理");

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
