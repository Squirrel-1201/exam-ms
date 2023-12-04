package exam.common.errorcode;

import lombok.AllArgsConstructor;
import ssm.common.exception.BusinessExceptionAssert;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/16 15:58
 */
@AllArgsConstructor
public enum QuestionErrorCode implements BusinessExceptionAssert {
    NOT_FIND_QUESTION_CATEGORY("2002","未查询到试题类别"),
    NOT_FIND_QUESTION("2003","未查询到该题目"),
    OPTION_TOO_LESS("2004","题目选项不足"),
    ANSWER_MISMATCH("2005","题目答案不匹配! 题目类型:{0},题目选项:{1},题目答案:{2}"),
    OPTION_IS_NOT_BLANK("2006","选择题选项内容错误! 问题类型:{0},问题选项:{1}"),
    JUDGE_NOT_OPTION("2007","判断题应不含选项"),
    TYPE_ERROR("2008","题目类型错误! 题目类型:{0},题目题干:{1},题目选项:{2}"),
    ANSWER_LENGTH_MISMATCH("2009","答案长度不匹配! 题目选项:{0},题目答案:{1}"),
    ANSWER_ILLEGAL("2010","非法答案! 题目选项:{0},题目答案:{1}"),
    ESSAY_NOT_OPTION("2016","简答题应不含选项"),
    FILE_IS_EMPTY("2017","上传的图片文件不能为空"),
    FILE_TOO_BIGGER("2108","上传图片文件过大，图片大小不能超过{0}"),
    IMAGE_TYPE_ERROR("2019","上传图片格式{0}不支持"),
    FILE_SAVE_FAIL("2020","图片文件保存异常{0}")
    ;

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
