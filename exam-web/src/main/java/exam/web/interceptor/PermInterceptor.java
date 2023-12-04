package exam.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import exam.bus.IResourceService;
import exam.bus.IUserRoleService;
import exam.bus.IUserService;
import exam.dao.entity.UserEntity;
import exam.common.enums.UserStatusEnum;
import exam.common.errorcode.AuthError;
import exam.web.jwt.JWTConfig;
import exam.web.jwt.JWTParam;
import exam.web.jwt.JWTUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ssm.common.constant.AuthConstant;
import ssm.common.entity.AuthInfoEntity;
import ssm.common.enums.AuthEntityType;
import ssm.common.exception.BaseException;
import ssm.common.exception.IResponseEnum;
import ssm.common.model.Result;
import ssm.common.util.HttpServletUtil;
import ssm.common.util.URLPathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class PermInterceptor implements HandlerInterceptor {

    private JWTUtil jwtUtil;

    private JWTConfig jwtConfig;

    private IUserService userService;

    private IResourceService resourceService;

    private IUserRoleService userRoleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String uri = request.getRequestURI();
            //判断资源是否需要登录校验
            if (!this.checkLoginVerify(uri)) {
                return true;
            }

            // 用户token校验
            JWTParam param = this.decodedToken(request);

            AuthInfoEntity authInfo = new AuthInfoEntity().setId(param.getId()).setName(param.getUsername());

            AuthEntityType type = AuthEntityType.USER;
            Long id = Long.valueOf(authInfo.getId().toString());
            //将用户信息写入header
            HttpServletUtil.reflectSetHeader(request, AuthConstant.AUTH_INFO, authInfo.encode());
            HttpServletUtil.reflectSetHeader(request, AuthConstant.AUTH_INFO_TYPE, String.valueOf(type.getCode()));

            //实时判断用户状态、权限
            this.check(id, uri, param.getRole(), type, response);

            String token = this.jwtUtil.generateToken(param);
            HttpServletUtil.addCookie(AuthConstant.TOKEN, token, this.jwtConfig.getExpire(), response);
            return true;
        } catch (SignatureVerificationException | AlgorithmMismatchException | TokenExpiredException e) {
            this.error(response, AuthError.NOT_LOGIN);
            return false;
        } catch (BaseException e) {
            this.error(response, e.getResponseEnum());
            return false;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            this.error(response, AuthError.TOKEN_ERROR);
            return false;
        }
    }

    /**
     * 用户token校验
     *
     * @param request
     * @return
     */
    private JWTParam decodedToken(HttpServletRequest request) {
        String token = HttpServletUtil.getToken(request);
        AuthError.NOT_LOGIN.notBlank(token);
        return this.jwtUtil.decodedToken(token);
    }


    private void check(Long id, String uri, String role, AuthEntityType type, HttpServletResponse response) {
        if (type == AuthEntityType.OTHER) {
            return;
        }
        UserEntity entity = this.userService.getById(id);
        AuthError.USER_CANCEL.isFalse(entity.getStatus() == UserStatusEnum.CANCEL);
        AuthError.USER_DISABLE.isTrue(entity.getStatus() == UserStatusEnum.ENABLE);

        //校验用户角色是否变更
        List<Long> roleIds = this.userRoleService.findByUserId(id);
        List<Long> sourceRoleIds = Arrays.stream(role.split(",")).map(Long::valueOf).collect(Collectors.toList());
        if (!roleIds.containsAll(sourceRoleIds)) {
            //退出登录
            HttpServletUtil.addCookie(AuthConstant.TOKEN, "false", 0, response);
            AuthError.ROLE_CHANGE.assertThrow();
        }

        //权限校验
        Set<String> perms = this.userService.getPerms(id);
        AuthError.NO_PERMISSION.isTrue(URLPathMatcher.match(perms, uri));
    }


    /**
     * 判断改地址是否需要uri校验
     *
     * @param uri
     * @return
     */
    private boolean checkLoginVerify(String uri) {
        //获取需要登录的资源
        List<String> urls = this.resourceService.getLoginResource();
        return URLPathMatcher.match(urls, uri);
    }


    private void error(HttpServletResponse response, IResponseEnum errorCode) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(Result.result(errorCode)));
    }

}
