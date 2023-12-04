package exam.common.errorcode;

import lombok.AllArgsConstructor;
import ssm.common.exception.BusinessExceptionAssert;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/22 14:18
 */
@AllArgsConstructor
public enum OrganizationError implements BusinessExceptionAssert {
    NOT_FIND_ORGANIZATION("4002", "组织机构不存在"),
    ORGANIZATION_NAME_EXIST("4003", "组织机构名称已存在"),
    CHILD_NODE_EXISTS("4004", "请先删除子节点"),
    USER_EXISTS("4005", "该机构下存在用户"),
    NOT_UPDATE_PARENT_ORG("4010", "不能变更上级机构");

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
