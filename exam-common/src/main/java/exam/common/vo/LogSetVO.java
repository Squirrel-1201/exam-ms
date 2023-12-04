package exam.common.vo;

import exam.common.enums.LogLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 日志设置表
 */
@Data
@Accessors(chain = true)
public class LogSetVO {

    @ApiModelProperty(value = "日志保留天数")
    @Min(value = 1, message = "日志保留天数最少保存1天")
    private Integer saveDay;

    @ApiModelProperty(value = "日志级别")
    @NotNull(message = "日志级别不能为空")
    private LogLevel level;
}
