package exam.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.exception.BusinessExceptionAssert;

@AllArgsConstructor
@Getter
public enum StuExamError implements BusinessExceptionAssert {

    EXAM_INFO_ERROR("400000", "考试信息错误"),
    EXAM_STATUS_ERROR("400002", "试卷状态错误"),
    EXAM_TIME_NOT_YET_ERROR("400004", "未到考试时间"),
    STU_INFO_ERROR("400010", "考生信息错误"),
    QUESTION_CATEGORY_ERROR("400020", "试题类别错误"),

    QUESTION_NOT_FUND("400040", "试题不存在"),
    QUESTION_DELETED("400050", "试题编号{0}已被删除"),
    EXAM_NOT_ALL_COMPLETED("400100", "还有{0}道题未完成"),
    STU_EXAM_NOT_FIND("400200", "考生试卷不存在"),
    STU_EXAM_STATUS_ERROR("400201", "考生状态错误{0},无法进行人工阅卷"),
    STU_EXAM_NOT_END_MARKING("400202", "考生试卷未全部完成阅卷，试卷编号: {0}"),
    EXAM_NOT_ALLOW_CHANGE_PC("400203", "考试过程中不允许切换电脑继续考试");

    String code;

    String message;
}
