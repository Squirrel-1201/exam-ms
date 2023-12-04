package exam.common.vo;

import exam.common.enums.LogLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LogVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "操作用户id")
    private String userId;

    @ApiModelProperty(value = "操作用户")
    private String username;

    @ApiModelProperty(value = "日志级别")
    private LogLevel level;

    @ApiModelProperty(value = "ip")
    private String ip;

    @ApiModelProperty(value = "动作")
    private String action;

    @ApiModelProperty(value = "详情")
    private String detail;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime generateTime;

    @ApiModelProperty(value = "操作结果 0 成功；其它失败")
    private String code;
}
