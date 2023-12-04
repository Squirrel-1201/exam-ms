package exam.web.controller.management;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IExamService;
import exam.bus.IExamStudentService;
import exam.common.enums.ExamStuStatusEnum;
import exam.common.vo.*;
import exam.common.enums.subtype.SysSubTypeEnum;
import exam.dao.entity.ExamEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: zhoufs
 * @Description: 试卷管理
 * @date 2022/2/18 16:08
 */
@RestController
@RequestMapping("admin/exam")
@Api(tags = "试卷管理")
@AllArgsConstructor
public class ExamController {

    private IExamService examService;

    private IExamStudentService examStudentService;


    @PostMapping("add")
    @ApiOperation("添加试卷")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "EXAM", action = "添加试卷", detailSpEL = "'试卷名称:'+ #vo.name")
    public Result addExam(@RequestBody @Valid ExamVO vo) {
        this.examService.add(vo);
        return Result.ok();
    }

    @PutMapping("update")
    @ApiOperation("修改试卷")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "EXAM", action = "修改试卷", detailSpEL = "'试卷id:'+ #vo.id +'试卷名称:'+ #vo.name")
    public Result updateExam(@RequestBody @Valid ExamVO vo) {
        this.examService.update(vo);
        return Result.ok();
    }

    @PostMapping("publish/{id}")
    @ApiOperation("发布试卷")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "EXAM", action = "发布试卷", detailSpEL = "'试卷名称:' + #result?.data + ';试卷id:'+ #id ")
    public Result<String> publishExam(@PathVariable Long id) {
        return Result.ok(this.examService.publishExam(id));
    }


    @DeleteMapping("delete-batch")
    @ApiOperation("批量删除试卷")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "EXAM", action = "删除试卷", detailSpEL = "'试卷ids:'+ #ids ")
    public Result deleteBatchExam(@RequestBody @Valid @NotEmpty Set<Long> ids) {
        this.examService.delete(ids);
        return Result.ok();
    }

    @PostMapping("select-stu")
    @ApiOperation("选择考试考生")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "EXAM", action = "选择考试考生", detailSpEL = "'试卷名称:' + #result?.data + ';试卷id:'+ #vo.examId")
    public Result<String> addExamStudent(@RequestBody @Valid ExamStudentAddVO vo) {
        return Result.ok(this.examStudentService.addExamStudent(vo));
    }

    @GetMapping("get-stu/{examId}")
    @ApiOperation("查看试卷考生")
    @ApiVersion
    public Result findStudentInfo(@PathVariable Long examId) {
        return Result.ok(this.examStudentService.findStudentsByExamId(examId));
    }


    @GetMapping("get-info/{id}")
    @ApiOperation("获取试卷详情")
    @ApiVersion
    public Result<ExamVO> findExamInfo(@PathVariable Long id) {
        return Result.ok(this.examService.findExamInfo(id));
    }

    @GetMapping("get-page")
    @ApiOperation("分页查询")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<Page<ExamEntity>> pageList(ExamQueryVO vo, @ApiIgnore Page page) {
        return Result.ok(this.examService.pageList(vo, page));
    }

    @GetMapping("get-marking")
    @ApiOperation("分页查询需要人工阅卷的试卷")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<Page<ArtificialMarkExamResultVO>> getMarkingExamList(ArtificialMarkQueryVO vo, @ApiIgnore Page page) {
        List<Integer> list = new ArrayList<>();
        list.add(ExamStuStatusEnum.COMPLETE.getCode());
        list.add(ExamStuStatusEnum.EXAM_MARKING.getCode());
        vo.setExamStuStatus(list);
        return Result.ok(this.examService.getMarkingExamList(vo, page));
    }

    @GetMapping("get-student-exam")
    @ApiOperation("分页查询考生试卷列表")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<Page<ArtificialMarkExamResultVO>> getStudentExamList(StuExamQueryVO vo, @ApiIgnore Page page) {
        return Result.ok(this.examService.getStudentExamList(vo, page));
    }

    @GetMapping("get-student-status")
    @ApiOperation("获取考生考试状态列表")
    @ApiVersion
    public Result<List<ComboBox>> comboBoxStudentStatus() {
        return Result.ok(this.examService.comboBoxStudentStatus());
    }

    @GetMapping("get-exam-status")
    @ApiOperation("获取试卷状态列表")
    @ApiVersion
    public Result<List<ComboBox>> comboBoxExamStatus() {
        return Result.ok(this.examService.comboBoxExamStatus());
    }

    @GetMapping("end/{examId}")
    @ApiOperation("结束考试")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "EXAM", action = "手动结束考试", detailSpEL = "'试卷名称:' + #result?.data + ';试卷id:'+ #examId ")
    public Result<String> endExam(@ApiParam("试卷id") @PathVariable("examId") Long examId) {
        return Result.ok(this.examService.endExam(examId));
    }

    @PostMapping("artificial")
    @ApiOperation("开始人工阅卷")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "EXAM", action = "开始人工阅卷", detailSpEL = "'试卷id:' + #vo.examId + ';考生id:'+ #vo.stuId ")
    public Result artificialMarkingExam(@RequestBody @Valid ArtificialMarkingVO vo) {
        this.examService.artificialMarkingExam(vo);
        return Result.ok();
    }

    @PostMapping("submit_marking")
    @ApiOperation("提交人工阅卷信息")
    @ApiVersion
    @OperLog(subTypeClass = SysSubTypeEnum.class, subType = "EXAM", action = "提交人工阅卷信息", detailSpEL = "'试卷id:'+ #vo.examId +'考生id:'+ #vo.stuId")
    public Result commitMarkingExam(@RequestBody @Valid SubmitMarkingResult vo) {
        this.examService.commitMarkingExam(vo);
        return Result.ok();
    }

    @GetMapping("get-exam-list")
    @ApiOperation("查询所有考试列表")
    @ApiVersion
    public Result<List<ExamEntity>> getNotEndExamList() {
        return Result.ok(examService.getNotEndExamList());
    }
}
