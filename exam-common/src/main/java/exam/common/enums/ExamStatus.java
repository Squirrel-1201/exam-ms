package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

@AllArgsConstructor
@Getter
public enum ExamStatus implements BaseEnum {

    UN_PUBLISHED(0, "未发布"),
    PUBLISHED(1, "已发布"),
    MARKING(2, "阅卷中"),
    END(3, "已结束"),
    ;


    @EnumValue
    private int code;

    private String desc;
}
