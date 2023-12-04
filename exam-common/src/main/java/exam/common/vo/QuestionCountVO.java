package exam.common.vo;

import exam.common.enums.QuestionTypeEnum;
import lombok.Data;

/**
 * @version 1.0.0
 * @date: 2022/7/5 11:20
 * @author: yangbo
 */
@Data
public class QuestionCountVO {

    private Long id;

    private Integer count;

    private QuestionTypeEnum type;
}
