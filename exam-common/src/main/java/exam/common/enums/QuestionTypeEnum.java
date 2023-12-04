package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

@AllArgsConstructor
@Getter
public enum QuestionTypeEnum implements BaseEnum {

    SINGLE(0, "单选题"),
    MULTIPLE(1, "多选题"),
    JUDGE(2, "判断题"),
    ESSAY(3,"简答题");

    @EnumValue
    private int code;

    private String desc;
}
