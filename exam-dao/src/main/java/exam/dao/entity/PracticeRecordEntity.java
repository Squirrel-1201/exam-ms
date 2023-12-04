package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @version 1.0.0
 * @date: 2022/7/13 16:03
 * @author: yangbo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("practice_record")
@ApiModel(value = "PracticeRecordEntity对象", description = "练习试题记录")
@Accessors(chain = true)
public class PracticeRecordEntity extends BaseEntity{

    private Long practiceId;

    private Long questionId;

    private Long practicePaperId;

    /**
     * 题型： 0单选 1 多选 2判断
     */
    private QuestionTypeEnum type;

    private String title;

    /**
     * 试题难度：0简单 1普通 2困难
     */
    private QuestionDifficultyEnum difficulty;

    /**
     * 参考答案
     */
    private String answer;

    /**
     * 考生答案
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String stuAnswer;

    private String analyse;

    private BigDecimal score;

    private BigDecimal finalScore;

}
