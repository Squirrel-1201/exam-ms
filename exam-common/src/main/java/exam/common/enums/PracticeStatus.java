package exam.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.enums.BaseEnum;

/**
 * @version 1.0.0
 * @date: 2022/7/13 10:43
 * @author: yangbo
 */
@AllArgsConstructor
@Getter
public enum PracticeStatus implements BaseEnum {

    UN_PUBLISH(0, "未发布"),
    PUBLISH(1, "已发布"),
    END(3, "已结束"),
    ;

    @EnumValue
    private int code;

    private String desc;
}
