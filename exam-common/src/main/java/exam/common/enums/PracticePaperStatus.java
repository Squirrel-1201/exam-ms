package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

/**
 * @version 1.0.0
 * @date: 2022/7/13 15:57
 * @author: yangbo
 */
@AllArgsConstructor
@Getter
public enum PracticePaperStatus implements BaseEnum {
    PRACTICE_ING(1, "正在练习中"),
    PRACTICE_FINISH(2, "已完成练习"),
    PASS(3, "通过"),
    NOT_PASS(4, "未通过");

    @EnumValue
    private int code;

    private String desc;
}
