package exam.dao.entity;

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
 * @date: 2022/7/13 14:40
 * @author: yangbo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("practice_question")
@ApiModel(value = "PracticeQuestionEntity对象", description = "练习问题表")
@Accessors(chain = true)
public class PracticeQuestionEntity extends BaseEntity {

    private Long practiceId;

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
