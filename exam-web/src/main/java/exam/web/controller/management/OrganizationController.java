package exam.web.controller.management;

import exam.bus.IOrganizationService;
import exam.common.vo.OrganizationTreeVO;
import exam.common.vo.OrganizationVO;
import exam.common.enums.subtype.SysSubTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ssm.common.annotation.ApiVersion;
import ssm.common.log.oper.OperLog;
import ssm.common.model.Result;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/22 14:32
 */
@RestController()
@RequestMapping("admin/organization")
@Api(tags = "组织机构管理")
@AllArgsConstructor
public class OrganizationController {

    private IOrganizationService organizationService;

    @PostMapping("add")
    @ApiOperation("添加组织")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "ORG", action = "添加组织机构", detailSpEL = "'机构名称:' + #vo.name")
    public Result add(@RequestBody @Valid OrganizationVO vo) {
        this.organizationService.add(vo);
        return Result.ok();
    }

    @PutMapping("update")
    @ApiOperation("修改组织")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "ORG", action = "修改组织机构", detailSpEL = "'机构名称:' + #vo.name + ',机构id:' + #vo.id")
    public Result update(@RequestBody @Valid OrganizationVO vo) {
        this.organizationService.update(vo);
        return Result.ok();
    }

    @DeleteMapping("delete/{id}")
    @ApiOperation("删除组织")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "ORG", action = "删除组织机构", detailSpEL = "'机构id:' + #id")
    public Result delete(@PathVariable("id") Long id) {
        this.organizationService.delete(id);
        return Result.ok();
    }

    @GetMapping("tree/user")
    @ApiOperation("组织机构树(包含用户)")
    @ApiVersion
    public Result<List<OrganizationTreeVO>> treeUser() {
        return Result.ok(this.organizationService.tree(true));
    }

    @GetMapping("tree")
    @ApiOperation("组织机构树")
    @ApiVersion
    public Result<List<OrganizationTreeVO>> tree() {
        return Result.ok(this.organizationService.tree(false));
    }

}
