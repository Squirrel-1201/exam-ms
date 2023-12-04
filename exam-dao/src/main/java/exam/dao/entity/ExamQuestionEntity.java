package exam.dao.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 试卷题目设置表
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("exam_question")
@Accessors(chain = true)
public class ExamQuestionEntity extends BaseEntity {

    private Long examId;

    private Long questionCategoryId;

    /**
     * 问题类型：0单选，1多选，2判断
     */
    private QuestionTypeEnum type;

    /**
     * 试题难度 0简单 1普通 2困难
     */
    private QuestionDifficultyEnum difficulty;

    private Integer num;

    private BigDecimal score;


}
