package exam.web.controller.dev;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IMenuService;
import exam.bus.IRoleService;
import exam.common.vo.MenuVo;
import exam.common.vo.RoleMenuVO;
import exam.common.vo.RoleVO;
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
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("dev/role")
@AllArgsConstructor
@Api(tags = "角色授权管理")
public class RoleController {

    private IMenuService menuService;

    private IRoleService roleService;

    @GetMapping("list")
    @ApiOperation("角色分页查询")
    @ApiVersion
    @ApiImplicitParams({@ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int")
            , @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<IPage<RoleVO>> pageList(RoleVO vo, @ApiIgnore Page page) {
        return Result.ok(this.roleService.pageList(vo, page));
    }

    @PostMapping("add")
    @ApiOperation("添加角色")
    @ApiVersion
    public Result add(@RequestBody @Valid RoleVO vo) {
        this.roleService.add(vo);
        return Result.ok();
    }

    @PutMapping("update")
    @ApiOperation("修改角色")
    @ApiVersion
    public Result update(@RequestBody @Valid RoleVO vo) {
        this.roleService.update(vo);
        return Result.ok();
    }

    @DeleteMapping("delete")
    @ApiOperation("删除角色")
    @ApiVersion
    public Result delete(@RequestBody @Valid @NotEmpty Set<Long> ids) {
        this.roleService.delete(ids);
        return Result.ok();
    }

    @PostMapping("auth")
    @ApiOperation("角色授权")
    @ApiVersion
    public Result auth(@RequestBody @Valid RoleMenuVO vo) {
        this.roleService.setRoleMenus(vo);
        return Result.ok();
    }

    @ApiOperation("根据角色id获取角色菜单列表")
    @ApiVersion
    @GetMapping("menu/{roleId}")
    public Result<List<MenuVo>> findMenuByRoleId(@PathVariable("roleId") Long roleId) {
        return Result.ok(this.menuService.findMenuByRoleId(roleId));
    }


}
