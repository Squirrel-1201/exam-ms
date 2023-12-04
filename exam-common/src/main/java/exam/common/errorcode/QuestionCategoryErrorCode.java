package exam.common.errorcode;

import lombok.AllArgsConstructor;
import ssm.common.exception.BusinessExceptionAssert;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/16 15:13
 */
@AllArgsConstructor
public enum QuestionCategoryErrorCode implements BusinessExceptionAssert {

    NOT_FIND_QUESTION_CATEGORY("1002", "未查询到试题类别"),
    CATEGORY_NAME_HAD_EXIST("1003", "试题类别名称不能重复"),
    CATEGORY_DISABLE("1004", "试题类别未启用"),
    CATEGORY_EXIST_QUESTION("1005", "请先删除对应类别的考题"),

    NOT_FIND_ROOT("1006", "未找到根节点"),
    CATEGORY_EXIST_CHILD_NODE("1007", "请先删除附属题目分类"),
    NOT_ALLOW_ID_EQUAL_PARENTID("1008", "不能将本ID作为父类ID绑定"),
    NOT_ALLOW_MODIFY_ROOT("1009", "不允许修改根节点"),
    NOT_ALLOW_MODIFY_CONTAINS_QUESTION("1010", "存在题目时不允许修改父节点");

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
