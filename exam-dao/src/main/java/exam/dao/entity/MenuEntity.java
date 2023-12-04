package exam.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import exam.common.enums.ENVEnum;
import exam.common.enums.MenuTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author zhoufs
 * @since 2022-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("menu")
@Accessors(chain = true)
public class MenuEntity extends BaseEntity {

    private Long parentId;

    private String menuName;

    private String routeId;

    private MenuTypeEnum type;

    private Integer orderNum;

    private String icon;

    private ENVEnum env;

    private String remark;

}
