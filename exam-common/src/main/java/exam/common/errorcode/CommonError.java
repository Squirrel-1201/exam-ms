package exam.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.exception.BusinessExceptionAssert;

@AllArgsConstructor
@Getter
public enum CommonError implements BusinessExceptionAssert {

    NOT_NULL("202000", "{0}不能为空"),
    EXISTS("203000", "{0}已存在"),
    NOT_EXISTS("204000", "{0}不存在");

    String code;

    String message;
}
