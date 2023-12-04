package exam.web.controller.management;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IUserService;
import exam.common.enums.subtype.SysSubTypeEnum;
import exam.common.vo.ChangeUserOrgVO;
import exam.common.vo.UserAddVO;
import exam.common.vo.UserRequestQueryVO;
import exam.common.vo.UserResponseVO;
import exam.common.vo.UserStatusUpdateVO;
import exam.common.vo.UserUpdateVO;
import exam.common.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import ssm.common.annotation.ApiVersion;
import ssm.common.log.oper.OperLog;
import ssm.common.model.Result;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/24 17:38
 */

@RestController
@RequestMapping("admin/user")
@Api(tags = "用户管理")
@AllArgsConstructor
public class UserController {

    private IUserService userService;

    @PostMapping("add")
    @ApiOperation("创建用户")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "USER", action = "创建用户", detailSpEL = "'用户名:' + #vo.username")
    public Result addUser(@RequestBody @Valid UserAddVO vo) {
        this.userService.addUser(vo);
        return Result.ok();
    }

    @PutMapping("update")
    @ApiOperation("修改用户")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "USER", action = "修改用户", detailSpEL = "'用户名:'+ #result?.data + ';用户id:'+#vo.id")
    public Result<String> updateUser(@RequestBody @Valid UserUpdateVO vo) {
        return Result.ok(this.userService.updateUser(vo));
    }

    @PostMapping("change-status")
    @ApiOperation("启用/禁用 用户")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "USER", action = "启用/禁用 用户", detailSpEL = "'用户名:'+ #result?.data + ';用户id:'+ #vo.id")
    public Result<String> changeUserStatus(@RequestBody @Valid UserStatusUpdateVO vo) {
        return Result.ok(this.userService.changeUserStatus(vo));
    }

    @PostMapping("cancel/{id}")
    @ApiOperation("注销用户")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "USER", action = "注销用户", detailSpEL = "'用户名:'+ #result?.data + ';用户id:'+ #id")
    public Result<String> cancel(@PathVariable("id") Long id) {
        return Result.ok(this.userService.cancelUser(id));
    }

    @PostMapping("cancel-batch")
    @ApiOperation("批量注销用户")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "USER", action = "注销用户", detailSpEL = "'用户ids:'+ #ids")
    public Result<String> cancelBatch(@RequestBody @Valid @NotEmpty Set<Long> ids) {
        this.userService.cancelBatchUser(ids);
        return Result.ok();
    }

    @GetMapping("rest/{id}")
    @ApiOperation("重置密码")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "USER", action = "重置用户密码", detailSpEL = "'用户名:'+ #result?.data + ';用户id:'+ #id")
    public Result<String> restPwd(@PathVariable Long id) {
        return Result.ok(this.userService.restUserPwd(id));
    }

    @PostMapping("change-org")
    @ApiOperation("重置用户组织机构")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "USER", action = "修改用户组织机构", detailSpEL = "'用户名:'+ #result?.data + ';用户id:'+ #vo.userId +',机构id:' + #vo.orgId")
    public Result changeOrg(@Valid @RequestBody ChangeUserOrgVO vo) {
        return Result.ok(this.userService.changeOrg(vo));
    }

    @GetMapping("get-info/{id}")
    @ApiOperation("获取用户信息")
    @ApiVersion
    public Result<UserVO> getUserInfo(@PathVariable Long id) {
        return Result.ok(this.userService.findUserInfoById(id));
    }

    @GetMapping("get-page")
    @ApiOperation("分页查询")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<Page<UserResponseVO>> pageListByMapper(UserRequestQueryVO vo, @ApiIgnore Page page) {
        return Result.ok(this.userService.pageListByMapper(vo, page));
    }
}
