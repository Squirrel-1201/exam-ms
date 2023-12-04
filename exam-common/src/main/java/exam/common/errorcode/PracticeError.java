package exam.common.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ssm.common.exception.BusinessExceptionAssert;

/**
 * @version 1.0.0
 * @date: 2022/7/13 11:31
 * @author: yangbo
 */

@Getter
@AllArgsConstructor
public enum PracticeError implements BusinessExceptionAssert {

    PRACTICE_SCORE_GE_PASS_SCORE("6001", "总分数必须大于等于通过分数"),
    PRACTICE_START_TIME_ERROR("6002", "练习开始日期必须大于当前日期"),
    PRACTICE_END_TIME_ERROR("6003", "练习结束时间不能早于开始时间"),
    PRACTICE_TOTAL_TIME_ERROR("6004", "练习总时间设置错误"),
    NOT_FIND_QUESTION("6005", "练习题目不能为空"),
    PRACTICE_NAME_HAD_EXIST("6006", "练习名称已存在"),
    SUM_SCORE_ERROR("6007", "总分数错误"),
    PRACTICE_CATEGORY_REPETITION("6008", "试题类别不能重复"),
    CATEGORY_ERROR("6009", "试题类别错误"),
    PRACTICE_NOT_FIND("6010", "练习不存在"),
    PRACTICE_PUBLISH_FAIL("6011", "只有未发布的试卷才能操作,该试卷目前状态为:{0}"),
    PRACTICE_STATUS_ERROR("6012", "{0}的练习不能执行此操作"),
    NOW_TIME_LE_END_TIME_ERROR("6013", "当前时间距离考试结束时间小于考试持续时间"),
    EXAM_PUBLISH_NO_STUDENT("6014", "练习未选择考生,发布失败"),
    ;

    String code;

    String message;
}
