package exam.web.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @version 1.0.0
 * @date: 2022/8/4 15:10
 * @author: yangbo
 */
@Data
public class SystemInfoConstant {

    private final static SystemInfoConstant INSTANCE = new SystemInfoConstant();

    private SystemInfoConstant() {
    }

    public static SystemInfoConstant getInstance() {
        return INSTANCE;
    }

    @ApiModelProperty(value = "系统版本")
    private String systemVersion = "V1.2.0";

    @ApiModelProperty(value = "系统标识")
    private String systemMark = "GTWX-EXAM-001";

    @ApiModelProperty(value = "系统名称")
    private String systemName = "考试管理系统";

    @ApiModelProperty(value = "更新时间")
    private String updateTime = "2022-09-20";
}
