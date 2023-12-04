package exam.common.vo;

import exam.common.enums.ExamStatus;
import exam.common.enums.QuestionTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StuExamQuestionVO {

    private Long examId;

    private Long stuId;

    private Long questionId;

    private QuestionTypeEnum type;

    private ExamStatus examStatus;

    private String title;

    private String answer;

    private String analyse;

    private String stuAnswer;

    private BigDecimal score;

    private BigDecimal finalScore;
}
