package exam.web.controller.management;

import exam.bus.ILogSetService;
import exam.common.enums.LogLevel;
import exam.common.vo.LogSetVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ssm.common.annotation.ApiVersion;
import ssm.common.entity.ComboBox;
import ssm.common.model.Result;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("admin/log-set")
@Api(tags = "日志设置")
public class LogSetController {

    private ILogSetService logSetService;

    @GetMapping("detail")
    @ApiVersion
    @ApiOperation("查询日志设置信息")
    public Result<LogSetVO> detail() {
        return Result.ok(this.logSetService.detail());
    }

    @PostMapping("update")
    @ApiVersion
    @ApiOperation("修改日志设置信息")
    public Result update(@RequestBody @Valid LogSetVO vo) {
        this.logSetService.update(vo);
        return Result.ok();
    }

    @GetMapping("comboBox")
    @ApiVersion
    @ApiOperation("日志级别下拉款")
    public Result comboBox() {
        List<ComboBox> list = Arrays.stream(LogLevel.values()).map(level ->
                        new ComboBox().setValue(String.valueOf(level.getCode())).setLabel(level.getDesc()))
                .collect(Collectors.toList());
        return Result.ok(list);
    }
}
