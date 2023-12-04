package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 考生试卷-题库选项表
 */
@Data
@TableName("exam_record_option")
@Accessors(chain = true)
public class ExamRecordOptionEntity extends BaseEntity {

    private Long examRecordId;

    private Long examId;

    private Long stuId;

    private String serialNo;

    private String title;

}
