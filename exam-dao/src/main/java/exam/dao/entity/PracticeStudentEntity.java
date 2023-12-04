package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @version 1.0.0
 * @date: 2022/7/13 16:16
 * @author: yangbo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("practice_student")
@ApiModel(value = "PracticeStudentEntity对象", description = "练习关联考生表")
@Accessors(chain = true)
public class PracticeStudentEntity extends BaseEntity{
    @ApiModelProperty(value = "考生id")
    private Long stuId;

    @ApiModelProperty(value = "练习id")
    private Long practiceId;

    @ApiModelProperty(value = "练习过的次数")
    private Integer usedTimes;
}
