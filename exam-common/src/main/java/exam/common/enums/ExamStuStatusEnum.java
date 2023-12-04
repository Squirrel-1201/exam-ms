package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

@AllArgsConstructor
@Getter
public enum ExamStuStatusEnum implements BaseEnum {

    TO_BE_EXAM(0, "待考"),
    EXAM_ING(1, "考试中"),
    COMPLETE(2, "已交卷"),
    PASS(3, "通过"),
    NOT_PASS(4, "未通过"),
    MISS_EXAM(5, "缺考"),
    EXAM_MARKING(6, "阅卷中");

    @EnumValue
    private int code;

    private String desc;

}
