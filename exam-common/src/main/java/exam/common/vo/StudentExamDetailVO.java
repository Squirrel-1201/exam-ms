package exam.common.vo;

import cn.hutool.core.collection.CollUtil;
import exam.common.enums.ExamStatus;
import exam.common.enums.ExamStuStatusEnum;
import exam.common.enums.QuestionTypeEnum;
import exam.common.enums.YNEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class StudentExamDetailVO {

    @ApiModelProperty(value = "考试剩余时间（秒）")
    private Long remainingSecond;

    @ApiModelProperty(value = "考生用户名")
    private String username;

    @ApiModelProperty(value = "考生真实姓名")
    private String realName;

    @ApiModelProperty(value = "试卷状态0 未发布 1已发布 2阅卷中 3已结束")
    private ExamStatus examStatus;

    @ApiModelProperty(value = "考试状态: 0 待考，1正在考试，2已交卷，3通过，4未通过，5缺考")
    private ExamStuStatusEnum examStuStatus;

    @ApiModelProperty(value = "试卷的id")
    private Long examId;

    @ApiModelProperty(value = "试卷的名称")
    private String examName;

    @ApiModelProperty(value = "试卷总分")
    private BigDecimal examScore;

    @ApiModelProperty(value = "考生成绩")
    private BigDecimal stuScore;

    @ApiModelProperty(value = "试题集合")
    private List<QuestionDetailVO> list = CollUtil.newArrayList();

    @Data
    @Accessors(chain = true)
    public static class QuestionDetailVO {

        @ApiModelProperty(value = "问题类型：0单选，1多选，2判断")
        private QuestionTypeEnum type;

        @ApiModelProperty(value = "题干")
        private String title;

        @ApiModelProperty(value = "问题分数")
        private BigDecimal score;

        @ApiModelProperty(value = "选项集合")
        private List<QuestionOptionDetailVO> options;

        @ApiModelProperty(value = "考生答案")
        private String stuAnswer;

        @ApiModelProperty(value = "考生得分")
        private BigDecimal stuScore;

        @ApiModelProperty(value = "答题状态0错误，1正确")
        private YNEnum status;

        @ApiModelProperty(value = "参考答案")
        private String answer;

        @ApiModelProperty(value = "答案解析")
        private String analyse;

        @ApiModelProperty(value = "问题id")
        private Long recordId;

    }


    @Data
    @Accessors(chain = true)
    public static class QuestionOptionDetailVO {

        @ApiModelProperty(value = "选项序列号")
        private String serialNo;

        @ApiModelProperty(value = "选项")
        private String option;
    }

}
