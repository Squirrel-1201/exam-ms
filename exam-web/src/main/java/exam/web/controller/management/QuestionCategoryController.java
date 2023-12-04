package exam.web.controller.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IQuestionCategoryService;
import exam.common.vo.QuestionCategoryAddVO;
import exam.common.vo.QuestionCategoryTreeVO;
import exam.common.vo.StatusUpdateVO;
import exam.common.vo.QuestionCategoryUpdateVO;
import exam.common.enums.subtype.SysSubTypeEnum;
import exam.dao.entity.QuestionCategoryEntity;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import ssm.common.annotation.ApiVersion;
import ssm.common.entity.ComboBox;
import ssm.common.log.oper.OperLog;
import ssm.common.model.Result;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/16 15:42
 */
@RestController
@RequestMapping("admin/question-category")
@Api(tags = "考题类目管理")
@AllArgsConstructor
public class QuestionCategoryController {

    private IQuestionCategoryService questionCategoryService;

    @PostMapping("add")
    @ApiOperation("添加考题类目")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "CATEGORY", action = "添加考题类目", detailSpEL = "'类目名称:'+ #vo.name")
    public Result addQuestionCategory(@RequestBody @Valid QuestionCategoryAddVO vo) {
        this.questionCategoryService.add(vo);
        return Result.ok();
    }

    @PutMapping("update")
    @ApiOperation("修改考题类目")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "CATEGORY", action = "修改考题类目", detailSpEL = "'类目名称:'+ #vo.name +',类目id:'+ #vo.id")
    public Result updateQuestionCategory(@RequestBody @Valid QuestionCategoryUpdateVO vo) {
        this.questionCategoryService.update(vo);
        return Result.ok();
    }

    @PostMapping("change-status")
    @ApiOperation("修改类目状态")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "CATEGORY", action = "修改考题类目状态", detailSpEL = "'类目名称:'+ #result?.data +',类目id:'+ #vo.id +',类目状态:'+ #vo.status")
    public Result changeStatus(@RequestBody @Valid StatusUpdateVO vo) {
        return Result.ok(this.questionCategoryService.changeStatus(vo));
    }

    @DeleteMapping("delete/{id}")
    @ApiOperation("删除考题类目")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "CATEGORY", action = "删除考题类目", detailSpEL = "'类目id:'+ #id")
    public Result deleteQuestionCategory(@PathVariable Long id) {
        this.questionCategoryService.delete(id);
        return Result.ok();
    }

    @DeleteMapping("delete-batch")
    @ApiOperation("批量删除考题类目")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "CATEGORY", action = "删除考题类目", detailSpEL = "'类目ids:'+ #ids")
    public Result deleteBatchQuestionCategory(@RequestBody @Valid @NotEmpty Set<Long> ids) {
        this.questionCategoryService.delete(ids);
        return Result.ok();
    }

    @GetMapping("combo-Box")
    @ApiOperation("获取已启用考题类目")
    @ApiVersion
    public Result<List<ComboBox>> comboBox() {
        return Result.ok(this.questionCategoryService.comboBoxEnableCategory());
    }

    @GetMapping("get-list")
    @ApiOperation("获取所有考题类目")
    @ApiVersion
    public Result<List<QuestionCategoryUpdateVO>> getList() {
        return Result.ok(this.questionCategoryService.getCategoryList());
    }

    @GetMapping("get-page")
    @ApiOperation("分页查询")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<IPage<QuestionCategoryUpdateVO>> pageList(QuestionCategoryAddVO vo, @ApiIgnore Page page) {
        return Result.ok(this.questionCategoryService.page(vo, page));
    }

    @GetMapping("tree")
    @ApiOperation("考题类目查询")
    @ApiVersion
    public Result<List<QuestionCategoryTreeVO>> getTree() {
        return Result.ok(questionCategoryService.tree());
    }

    @GetMapping("get-all-category")
    @ApiOperation("查询所有已存在考题分类")
    @ApiVersion
    public Result<List<QuestionCategoryEntity>> getAllInfo(){
        return Result.ok(questionCategoryService.queryAllCategory());
    }
}
