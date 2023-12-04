package exam.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.exception.BusinessExceptionAssert;

/**
 * @version 1.0.0
 * @date: 2022/7/15 17:06
 * @author: yangbo
 */
@AllArgsConstructor
@Getter
public enum PracticePaperError implements BusinessExceptionAssert {
    PRACTICE_CONTINUE("7001", "练习已存在"),
    PRACTICE_NOT_FIND("7002", "练习不存在"),
    PRACTICE_NOT_ALLOW_STUDENT("7003", "考生不允许使用此次练习"),
    PRACTICE_OVER_TIME("7004", "已超过练习时间"),
    PRACTICE_OVER_TIMES("7005", "超过练习次数"),
    PRACTICE_STATUS_ERROR("7006", "练习状态错误，请确认是否可以练习"),
    PAPER_NOT_FIND("7007", "试卷不存在"),
    STUDENT_NOT_FIND("7008","考生不存在"),
    PRACTICE_PSTATUS_ERROR("7009", "练习状态错误"),
    PRACTICE_NOT_ALL_COMPLETED("7010", "还有{0}道题未完成"),
    ;


    String code;

    String message;
}
