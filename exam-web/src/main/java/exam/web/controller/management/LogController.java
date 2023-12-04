package exam.web.controller.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.ILogService;
import exam.common.vo.LogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import ssm.common.annotation.ApiVersion;
import ssm.common.model.Result;

@AllArgsConstructor
@RestController
@RequestMapping("admin/log")
@Api(tags = "日志管理")
public class LogController {

    private ILogService logService;

    @GetMapping("list")
    @ApiOperation("分页查询")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "ip", value = "ip", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "string")})
    public Result<IPage<LogVO>> pageList(@ApiIgnore Page page, @ApiIgnore LogVO vo) {
        return Result.ok(this.logService.pageList(page, vo));
    }

}
