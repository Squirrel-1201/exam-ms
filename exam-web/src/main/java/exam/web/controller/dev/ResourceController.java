package exam.web.controller.dev;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IResourceService;
import exam.common.vo.ResourceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import ssm.common.annotation.ApiVersion;
import ssm.common.model.Result;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@RestController
@RequestMapping("dev/resource")
@AllArgsConstructor
@Api(tags = "资源管理")
public class ResourceController {

    private IResourceService resourceService;

    @GetMapping("list")
    @ApiOperation("角色分页查询")
    @ApiVersion
    @ApiImplicitParams({@ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int")
            , @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<IPage<ResourceVO>> pageList(ResourceVO vo, @ApiIgnore Page page) {
        return Result.ok(this.resourceService.pageList(vo, page));
    }

    @PostMapping("add")
    @ApiOperation("添加资源")
    @ApiVersion
    public Result add(@RequestBody @Valid ResourceVO vo) {
        this.resourceService.add(vo);
        return Result.ok();
    }

    @PutMapping("update")
    @ApiOperation("修改资源")
    @ApiVersion
    public Result update(@RequestBody @Valid ResourceVO vo) {
        this.resourceService.update(vo);
        return Result.ok();
    }

    @DeleteMapping("delete")
    @ApiOperation("删除资源")
    @ApiVersion
    public Result delete(@RequestBody @Valid @NotEmpty Set<Long> ids) {
        this.resourceService.delete(ids);
        return Result.ok();
    }
}
