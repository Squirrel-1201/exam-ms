package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 题库选项表
 */
@Data
@TableName("question_option")
@Accessors(chain = true)
public class QuestionOptionEntity extends BaseEntity {

    private Long questionId;

    private String serialNo;

    private String title;

}
