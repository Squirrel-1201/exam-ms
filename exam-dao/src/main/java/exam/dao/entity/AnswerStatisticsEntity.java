package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @version 1.0.0
 * @date: 2022/7/6 15:35
 * @author: yangbo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("answer_statistics")
@ApiModel(value = "ExamEntity对象", description = "试卷表")
@Accessors(chain = true)
public class AnswerStatisticsEntity extends BaseEntity {

    private Long questionId;

    private Integer errorCount;

    private Integer totalCount;
}
