package exam.web.controller.common;

import exam.bus.IMenuService;
import exam.common.vo.MenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssm.common.annotation.ApiVersion;
import ssm.common.model.Result;
import ssm.common.util.AuthInfoUtil;

import java.util.List;

@RestController
@RequestMapping("common/user-menu")
@Api(tags = "用户菜单管理")
@AllArgsConstructor
public class UserMenuController {

    private IMenuService menuService;

    @ApiOperation("获取当前登录用户树形菜单")
    @ApiVersion
    @GetMapping("tree")
    public Result<List<MenuVo>> currentUserMenu() {
        return Result.ok(this.menuService.findMenuByUserId((Long) AuthInfoUtil.getUserId()));
    }

    @ApiOperation("获取用户树形菜单")
    @ApiVersion
    @GetMapping("tree/{userId}")
    public Result<List<MenuVo>> getUserMenuById(@PathVariable Long userId) {
        return Result.ok(this.menuService.findMenuByUserId(userId));
    }
}
