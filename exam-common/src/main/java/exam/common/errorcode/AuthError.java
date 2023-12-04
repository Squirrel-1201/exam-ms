package exam.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.exception.BusinessExceptionAssert;

@AllArgsConstructor
@Getter
public enum AuthError implements BusinessExceptionAssert {

    CAPTCHA_CODE_ERROR("200000", "验证码错误"),
    USER_NOT_FUND("200010", "用户不存在"),
    PASSWORD_ERROR("200020", "口令错误,连续输入{0}次后将被锁定"),
    NOT_LOGIN("300000", "请先登录"),
    TOKEN_ERROR("300006","登录已失效,请重新登录"),
    NO_PERMISSION("300010", "越权访问"),
    USER_DISABLE("300020", "用户已被禁用"),
    USER_CANCEL("300030", "用户被注销"),
    USER_LOCK("300040", "用户已锁定,预计解锁时间：{0}"),
    ROLE_CHANGE("300050", "当前用户角色已变更，请重新登录"),
    ;
    String code;

    String message;
}
