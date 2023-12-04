package exam.common.errorcode;

import lombok.AllArgsConstructor;
import ssm.common.exception.BusinessExceptionAssert;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/24 14:49
 */
@AllArgsConstructor
public enum UserError implements BusinessExceptionAssert {
    REQUEST_PRAM_ERROR("5001", "请求参数异常"),
    USER_NAME_HAD_EXIST("5002","用户名已存在"),
    ORGANIZATION_NOT_EXIST("5003","组织机构不存在"),
    USER_ROLE_NOT_EXIST("5004","角色不存在"),
    USER_NOT_EXIST("5005","用户不存在"),
    STATUS_PARAM_ERROR("5006","状态参数错误"),
    USER_ENABLE_ERROR("5007","注销用户不可启用"),
    USER_OLD_PWD_ERROR("5008","旧口令错误");

    String code;
    String message;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
