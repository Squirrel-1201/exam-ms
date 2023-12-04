package exam.common.errorcode;

import lombok.AllArgsConstructor;
import ssm.common.exception.BusinessExceptionAssert;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/18 11:51
 */
@AllArgsConstructor
public enum ExamError implements BusinessExceptionAssert {
    EXAM_NAME_HAD_EXIST("3002", "试卷名称已存在"),
    NOT_FIND_EXAM("3003", "试卷不存在"),
    NOT_FIND_QUESTION("3004", "题目不能为空"),
    SUM_SCORE_ERROR("3007", "总分数错误"),
    EXAM_QUESTION_NOT_EXIST("3008", "试卷问题不存在"),
    EXAM_DELETE_FAIL("3009", "删除试卷失败,只能删除未发布的试卷"),
    EXAM_PUBLISH_FAIL("3010", "只有未发布的试卷才能操作,该试卷目前状态为:{0}"),
    EXAM_SCORE_GE_PASS_SCORE("3011", "总分数必须大于等于通过分数"),
    EXAM_PASS_SCORE_FAIL("3012", "通过分数错误"),
    EXAM_PUBLISH_NO_STUDENT("3013", "试卷未选择考生,发布失败"),
    EXAM_START_TIME_ERROR("3015", "考试开始日期必须大于当前日期"),
    EXAM_END_TIME_ERROR("3018", "考试结束时间不能早于开始时间"),
    EXAM_TOTAL_TIME_ERROR("3020", "考试总时间设置错误"),
    EXAM_CATEGORY_REPETITION("3025", "试题类别不能重复"),
    CATEGORY_ERROR("3030", "试题类别错误"),
    QUESTION_IS_NULL("3035", "请先录入考题"),
    STUDENT_CHOSE_EXAM_ERROR("3030", "试卷状态未发布时才能选择考生"),
    QUESTION_NUM_ERROR("3040", "题目数量值错误"),
    QUESTION_SCORE_ERROR("3045", "题目分值错误"),
    QUESTION_NUM_TOO_LESS("3050", "试题类别为‘{0}’的‘{1}’‘{2}’题型数量不足"),
    NOT_SELECTED_STU("3060", "请选择考生"),
    QUESTION_OPTION_ERROR("3070", "问题选项错误"),
    NOW_TIME_LE_END_TIME_ERROR("3080", "当前时间距离考试结束时间小于考试持续时间"),
    EXAM_STATUS_ERROR("3090", "{0}的试卷不能执行此操作"),
    EXAM_GET_SCORE_ERROR("3095","人工阅卷时题目编号{0}的题目获得分数不得大于题目总分数"),
    EXAM_GET_SCORE_NOT_NULL("3096","人工阅卷时存在题目还未评分"),
    ;

    String code;

    String message;

    @Override
    public String getCode() {
        return String.valueOf(this.code);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
