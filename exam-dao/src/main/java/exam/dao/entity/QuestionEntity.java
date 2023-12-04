package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import exam.common.enums.EnableEnum;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * <p>
 * 题库
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("question")
@ToString
public class QuestionEntity extends BaseEntity {

    private Long questionCategoryId;

    private EnableEnum status;

    /**
     * 题型： 0单选 1 多选 2判断
     */
    private QuestionTypeEnum type;

    private String title;

    private String answer;

    private String analyse;

    /**
     * 试题难度：0简单 1普通 2困难
     */
    private QuestionDifficultyEnum difficulty;

}
