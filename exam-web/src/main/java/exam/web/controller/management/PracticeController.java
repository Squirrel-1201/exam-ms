package exam.web.controller.management;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IPracticeService;
import exam.bus.IPracticeStudentService;
import exam.common.enums.subtype.SysSubTypeEnum;
import exam.common.vo.PracticePaperQueryVO;
import exam.common.vo.PracticeQueryVO;
import exam.common.vo.PracticeStudentAddVO;
import exam.common.vo.PracticeVO;
import exam.common.vo.PracticedPaperVO;
import exam.dao.entity.PracticeEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import ssm.common.annotation.ApiVersion;
import ssm.common.log.oper.OperLog;
import ssm.common.model.Result;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;


/**
 * @version 1.0.0
 * @date: 2022/7/13 9:19
 * @author: yangbo
 */
@RestController()
@RequestMapping("admin/practice")
@Api(tags = "练习管理")
@AllArgsConstructor
public class PracticeController {

    private IPracticeService practiceService;

    private IPracticeStudentService practiceStudentService;


    @PostMapping("add")
    @ApiOperation("添加练习")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "PRACTICE", action = "添加练习", detailSpEL = "'练习名称:'+ #vo.name")
    public Result addPractice(@RequestBody @Valid PracticeVO vo) {
        practiceService.add(vo);
        return Result.ok();
    }

    @PostMapping("update")
    @ApiOperation("修改练习")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "PRACTICE", action = "修改练习", detailSpEL = "'练习名称:'+ #vo.name")
    public Result updatePractice(@RequestBody @Valid PracticeVO vo) {
        practiceService.update(vo);
        return Result.ok();
    }

    @PostMapping("publish/{practiceId}")
    @ApiOperation("发布练习")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "PRACTICE", action = "发布练习", detailSpEL = "'练习名称：' + #result?.data + '练习id:'+ #practiceId")
    public Result<String> publishPractice(@PathVariable("practiceId") @NotNull(message = "必须传入练习ID") Long practiceId) {
        return Result.ok(practiceService.publishPractice(practiceId));
    }

    @DeleteMapping("delete-batch")
    @ApiOperation("批量删除练习")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "PRACTICE", action = "删除试卷", detailSpEL = "'试卷ids:'+ #ids ")
    public Result deleteBatchExam(@RequestBody @Valid @NotEmpty Set<Long> ids) {
        practiceService.delete(ids);
        return Result.ok();
    }

    @GetMapping("get-page")
    @ApiOperation("分页查询练习列表")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<Page<PracticeEntity>> pageList(PracticeQueryVO vo, @ApiIgnore Page page) {
        return Result.ok(practiceService.pageList(vo, page));
    }

    @GetMapping("end-practice/{practiceId}")
    @ApiOperation("结束练习")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "PRACTICE", action = "结束练习", detailSpEL = "'练习名称：' + #result?.data + '练习id:'+ #practiceId")
    public Result<String> endPractice(@ApiParam("练习ID") @PathVariable("practiceId") Long practiceId) {
        return Result.ok(practiceService.endPractice(practiceId));
    }

    @GetMapping("get-info/{id}")
    @ApiOperation("获取练习详情")
    @ApiVersion
    public Result<PracticeVO> findExamInfo(@PathVariable Long id) {
        return Result.ok(practiceService.findPracticeInfo(id));
    }


    @PostMapping("select-stu")
    @ApiOperation("选择练习考生")
    @ApiVersion
    public Result<String> addExamStudent(@RequestBody @Valid PracticeStudentAddVO vo) {
        return Result.ok(practiceStudentService.addPracticeStudent(vo));
    }

    @GetMapping("get-stu/{practiceId}")
    @ApiOperation("查看练习考生")
    @ApiVersion
    public Result findStudentInfo(@PathVariable("practiceId") Long practiceId) {
        return Result.ok(practiceStudentService.findStudentsByPracticeId(practiceId));
    }

    @GetMapping("get-practice-list")
    @ApiOperation("分页查询试卷列表")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<Page<PracticedPaperVO>> getPracticedList(PracticePaperQueryVO vo, @ApiIgnore Page page) {
        return Result.ok(practiceService.getPracticedList(vo, page));
    }

}
