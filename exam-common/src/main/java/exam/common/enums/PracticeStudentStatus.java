package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

/**
 * @version 1.0.0
 * @date: 2022/7/15 11:02
 * @author: yangbo
 */
@AllArgsConstructor
@Getter
public enum PracticeStudentStatus implements BaseEnum {
    UN_PRACTICE(0, "未练习"),
    PRACTICE_ING(1, "正在练习中");

    @EnumValue
    private int code;

    private String desc;
}
