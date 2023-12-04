package exam.common.enums.subtype;

import lombok.AllArgsConstructor;
import ssm.common.log.SubType;

/**
 * 考生考试
 */
@AllArgsConstructor
public enum StuExamSubTypeEnum implements SubType {

    STU_EXAM("20000", "考生考试"),
    STU_PRACTICE("20001", "练习考试");

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
