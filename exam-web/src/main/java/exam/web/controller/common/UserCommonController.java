package exam.web.controller.common;

import exam.bus.IUserService;
import exam.common.enums.subtype.SysSubTypeEnum;
import exam.common.vo.UserUpdatePwdVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssm.common.annotation.ApiVersion;
import ssm.common.log.oper.OperLog;
import ssm.common.model.Result;
import ssm.common.util.AuthInfoUtil;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/3/16 19:52
 */

@RestController
@RequestMapping("common/user")
@Api(tags = "用户公用管理")
@AllArgsConstructor
public class UserCommonController {

    private IUserService userService;

    @PutMapping("update-pwd")
    @ApiOperation("修改用户密码")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "USER", action = "修改用户密码", detailSpEL = "'用户名:'+ #result?.data + ';用户id:'+#vo.id")
    public Result<String> updateUserPwd(@RequestBody @Valid UserUpdatePwdVO vo) {
        vo = Optional.ofNullable(vo).orElse(new UserUpdatePwdVO())
                .setId((Long) AuthInfoUtil.getUserId());
        return Result.ok(this.userService.updateUserPwd(vo));
    }
}
