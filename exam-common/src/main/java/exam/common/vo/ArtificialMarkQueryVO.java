package exam.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: zhoufs
 * @Date: 2022/6/16 14:47
 * @Description:  阅卷管理列表查询条件对象
 */
@Data
public class ArtificialMarkQueryVO {

    @ApiModelProperty(value = "考生用户名")
    private String username;

    @ApiModelProperty(value = "考生真实姓名")
    private String realName;

    @ApiModelProperty(value = "试卷的名称")
    private String examName;

    @ApiModelProperty(value = "考生考试状态",hidden = true)
    private List<Integer> examStuStatus;
}
