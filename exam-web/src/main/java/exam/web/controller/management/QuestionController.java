package exam.web.controller.management;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IQuestionService;
import exam.common.enums.subtype.SysSubTypeEnum;
import exam.common.vo.*;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
 * @date 2022/2/16 16:23
 */
@RestController
@RequestMapping("admin/question")
@Api(tags = "题库管理")
@AllArgsConstructor
public class QuestionController {

    private IQuestionService questionService;


    @PostMapping("add")
    @ApiOperation("添加题目")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "QUESTION", action = "添加题目", detailSpEL = "'题目类别名称:'+ #result?.data +',题目类别id:'+ #vo.questionCategoryId +'题目类型:'+ #vo.type")
    public Result add(@RequestBody @Valid QuestionVO vo) {
        return Result.ok(this.questionService.add(vo));
    }

    @PostMapping("batch-add")
    @ApiOperation("批量添加题目")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "QUESTION", action = "批量添加题目")
    public Result batchAdd(@RequestBody @Valid @NotEmpty List<QuestionVO> list) {
        this.questionService.batchAdd(list);
        return Result.ok();
    }

    @PostMapping("upload-image")
    @ApiOperation("题目图片上传")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "QUESTION", action = "题目图片上传")
    public Result uploadImage(MultipartFile file) {
        return Result.ok(this.questionService.uploadImage(file));
    }

    @GetMapping("get-image-prefix")
    @ApiOperation("获取服务路径")
    @ApiVersion
    public Result getServerPath() {
        return Result.ok(this.questionService.getServerPath());
    }

    @PutMapping("update")
    @ApiOperation("修改题目")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "QUESTION", action = "修改题目", detailSpEL = "'题目类别名称:'+ #result?.data +',题目id:'+ #vo.id +',题目类别id:'+ #vo.questionCategoryId +',题目类型:'+ #vo.type")
    public Result update(@RequestBody @Valid QuestionVO vo) {
        return Result.ok(this.questionService.update(vo));
    }

    @PostMapping("change-status")
    @ApiOperation("修改题目状态")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "QUESTION", action = "修改题目状态", detailSpEL = "'题目id:'+ #vo.id +',题目状态:' + #vo.status")
    public Result changeStatus(@RequestBody @Valid StatusUpdateVO vo) {
        this.questionService.changeStatus(vo);
        return Result.ok();
    }

    @DeleteMapping("delete/{id}")
    @ApiOperation("删除题目")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "QUESTION", action = "删除题目", detailSpEL = "'题目id:'+ #id")
    public Result delete(@PathVariable Long id) {
        this.questionService.delete(id);
        return Result.ok();
    }

    @DeleteMapping("delete-batch")
    @ApiOperation("批量删除题目")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "QUESTION", action = "删除题目", detailSpEL = "'题目ids:'+ #ids")
    public Result deleteBatch(@RequestBody @Valid @NotEmpty Set<Long> ids) {
        this.questionService.delete(ids);
        return Result.ok();
    }

    @GetMapping("question-type-Box")
    @ApiOperation("获取问题类型列表")
    @ApiVersion
    public Result<List<ComboBox>> comboBoxQuestionType() {
        return Result.ok(this.questionService.comboBoxQuestionType());
    }

    @GetMapping("question-difficulty-Box")
    @ApiOperation("获取问题难易程度列表")
    @ApiVersion
    public Result<List<ComboBox>> comboBoxQuestionDifficulty() {
        return Result.ok(this.questionService.comboBoxQuestionDifficulty());
    }

    @GetMapping("count-question/{id}")
    @ApiOperation("查询类目下题目数量")
    @ApiVersion
    public Result<List<QuestionTypeCountVO>> countQuestionEnable(@PathVariable Long id) {
        return Result.ok(this.questionService.countQuestion(id));
    }

    @GetMapping("get-info/{id}")
    @ApiOperation("查询题目内容")
    @ApiVersion
    public Result<QuestionVO> findQuestionById(@PathVariable Long id) {
        return Result.ok(this.questionService.findById(id));
    }

    @GetMapping("get-page")
    @ApiOperation("分页查询")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<Page<QuestionResultVO>> pageList(QuestionQueryVO vo, @ApiIgnore Page page) {
        return Result.ok(this.questionService.pageListByMapper(vo, page));
    }

    @PostMapping("check-duplicate")
    @ApiOperation("题干查重")
    @ApiVersion
    public Result<Boolean> checkDuplicate(@RequestBody @NotEmpty(message = "必须输入题干") String title) {
        return Result.ok(questionService.checkDuplicate(title));
    }
}
