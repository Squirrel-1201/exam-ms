package exam.web.controller;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import exam.bus.IUserService;
import exam.common.vo.LoginReqVO;
import exam.common.vo.LoginResVO;
import exam.common.enums.subtype.LoginSubTypeEnum;
import exam.web.jwt.JWTConfig;
import exam.web.jwt.JWTParam;
import exam.web.jwt.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import ssm.common.annotation.ApiVersion;
import ssm.common.captcha.CaptchaService;
import ssm.common.constant.AuthConstant;
import ssm.common.log.login.LoginLog;
import ssm.common.model.Result;
import ssm.common.util.HttpServletUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping
@Api(tags = "用户登录管理")
@AllArgsConstructor
public class LoginController {

    private IUserService userService;

    private CaptchaService captchaService;

    private JWTConfig jwtConfig;

    private JWTUtil jwtUtil;

    /**
     * 获取验证码
     *
     * @return
     */
    @ApiOperation("获取验证码")
    @GetMapping("captcha")
    @ApiVersion
    public Result captcha() {
        return Result.ok(this.captchaService.createCaptcha(RandomUtil.randomString(16)));
    }

    @ApiOperation("用户登录")
    @PostMapping("login")
    @ApiVersion
    @LoginLog(subTypeClass = LoginSubTypeEnum.class, subType = "LOGIN", action = "用户登录", detail = "用户登录成功", userId = "#result?.data?.userId", userName = "#vo.username")
    public Result<LoginResVO> login(@RequestBody LoginReqVO vo, @ApiIgnore HttpServletResponse response) {
        LoginResVO user = this.userService.login(vo);
        String token = this.jwtUtil.generateToken(new JWTParam().setId(user.getUserId()).setUsername(user.getUsername()).setRole(user.getRole()));
        HttpServletUtil.addCookie(AuthConstant.TOKEN, token, this.jwtConfig.getExpire(), response);
        return Result.ok(user.setToken(token));
    }

    @ApiOperation("用户退出")
    @GetMapping("logout")
    @ApiVersion
    @LoginLog(subTypeClass = LoginSubTypeEnum.class, subType = "LOGOUT", action = "用户退出", detail = "用户退出成功",userId = "#result?.data?.userId", userName = "#result?.data?.username")
    public Result<LoginResVO> exit(@ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) {
        HttpServletUtil.addCookie(AuthConstant.TOKEN, "false", 0, response);
        String token = HttpServletUtil.getToken(request);
        if (CharSequenceUtil.isNotBlank(token)) {
            JWTParam param = this.jwtUtil.decodedToken(token);
            return Result.ok(new LoginResVO().setUserId(param.getId()).setUsername(param.getUsername()));
        }
        return Result.ok();
    }


}
