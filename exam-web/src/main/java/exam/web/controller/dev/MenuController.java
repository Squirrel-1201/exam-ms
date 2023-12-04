package exam.web.controller.dev;

import exam.bus.IMenuService;
import exam.common.vo.MenuVo;
import exam.common.vo.ResourceMenuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ssm.common.annotation.ApiVersion;
import ssm.common.model.Result;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("dev/menu")
@AllArgsConstructor
@Api(tags = "菜单管理")
public class MenuController {

    private IMenuService menuService;

    @GetMapping("tree")
    @ApiOperation("菜单tree")
    @ApiVersion
    public Result<List<MenuVo>> list() {
        return Result.ok(this.menuService.findAllMenuTree());
    }

    @PostMapping("add-node")
    @ApiOperation("添加节点")
    @ApiVersion
    public Result addNode(@RequestBody @Valid MenuVo vo) {
        this.menuService.addNode(vo);
        return Result.ok();
    }

    @PutMapping("update")
    @ApiOperation("修改节点")
    @ApiVersion
    public Result update(@RequestBody @Valid MenuVo vo) {
        this.menuService.update(vo);
        return Result.ok();
    }

    @PostMapping("relation-resource")
    @ApiOperation("关联资源")
    @ApiVersion
    public Result relationResource(@RequestBody @Valid ResourceMenuVO vo) {
        this.menuService.relationResource(vo);
        return Result.ok();
    }

    @DeleteMapping("un-relation-resource")
    @ApiOperation("取消资源关联")
    @ApiVersion
    public Result unRelationResource(@RequestBody @Valid ResourceMenuVO vo) {
        this.menuService.unRelationResource(vo);
        return Result.ok();
    }
}
