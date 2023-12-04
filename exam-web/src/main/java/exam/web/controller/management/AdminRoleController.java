package exam.web.controller.management;

import exam.bus.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssm.common.annotation.ApiVersion;
import ssm.common.entity.ComboBox;
import ssm.common.model.Result;

import java.util.List;

@RestController
@RequestMapping("admin/role")
@Api(tags = "管理端-角色")
@AllArgsConstructor
public class AdminRoleController {

    private IRoleService roleService;

    @GetMapping("combo-box")
    @ApiVersion
    @ApiOperation("查询所有角色")
    public Result<List<ComboBox>> comboBox() {
        return Result.ok(this.roleService.comboBox());
    }

}
