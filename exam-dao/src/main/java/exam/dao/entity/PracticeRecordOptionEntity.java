package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @version 1.0.0
 * @date: 2022/7/13 16:09
 * @author: yangbo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("practice_record_option")
@ApiModel(value = "PracticeRecordOptionEntity对象", description = "练习试题选项")
@Accessors(chain = true)
public class PracticeRecordOptionEntity extends BaseEntity {

    private Long practiceRecordId;

    private Long practiceId;

    private Long practicePaperId;

    private String serialNo;

    private String title;

}
