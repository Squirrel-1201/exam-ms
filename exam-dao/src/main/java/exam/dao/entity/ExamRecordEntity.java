package exam.dao.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 考试记录
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("exam_record")
@Accessors(chain = true)
public class ExamRecordEntity extends BaseEntity {

    private Long examId;

    private Long questionId;

    private Long stuId;

    /**
     * 题型： 0单选 1 多选 2判断
     */
    private QuestionTypeEnum type;

    private String title;

    /**
     * 参考答案
     */
    private String answer;

    /**
     * 考生答案
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String stuAnswer;

    /**
     * 试题难度：0简单 1普通 2困难
     */
    private QuestionDifficultyEnum difficulty;

    private String analyse;

    private BigDecimal score;

    private BigDecimal finalScore;

}
