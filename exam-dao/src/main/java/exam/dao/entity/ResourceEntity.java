package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import exam.common.enums.YNEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 资源表
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("resource")
@ApiModel(value="ResourceEntity对象", description="资源表")
public class ResourceEntity extends BaseEntity {

    private String resourceName;

    private String url;

    private YNEnum loginVerify;

    private String remark;

}
