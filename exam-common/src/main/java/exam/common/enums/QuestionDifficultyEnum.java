package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

@AllArgsConstructor
@Getter
public enum QuestionDifficultyEnum implements BaseEnum {

    EASY(0, "容易"),
    GENERAL(1, "普通"),
    DIFFICULTY(2,"困难");

    @EnumValue
    private int code;

    private String desc;
}
