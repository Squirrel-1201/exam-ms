package exam.web.controller.common;

import exam.web.config.SystemInfoConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssm.common.annotation.ApiVersion;
import ssm.common.model.Result;

/**
 * @version 1.0.0
 * @date: 2022/8/4 15:22
 * @author: yangbo
 */
@AllArgsConstructor
@RestController
@RequestMapping("/common/system")
@Api(tags = "系统信息")
public class SystemController {

    @GetMapping("info")
    @ApiOperation("系统基本信息")
    @ApiVersion
    public Result<SystemInfoConstant> getSystemInfo() {
        return Result.ok(SystemInfoConstant.getInstance());
    }
}
