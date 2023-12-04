package exam.web.controller.student;

import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IStuExamService;
import exam.common.enums.subtype.StuExamSubTypeEnum;
import exam.common.vo.StudentExamDetailVO;
import exam.common.vo.StudentExamVO;
import exam.common.vo.SubmitAnswerVO;
import exam.common.vo.SubmitExamVO;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import ssm.common.annotation.ApiVersion;
import ssm.common.log.biz.BizLog;
import ssm.common.model.Result;
import ssm.common.util.AuthInfoUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("stu/my-exam")
@Api(tags = "我的考试")
public class MyExamController {

    private IStuExamService stuExamService;

    @GetMapping("plan-list")
    @ApiOperation("考试计划列表")
    @ApiVersion
    @ApiImplicitParams({@ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int")
            , @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<IPage<StudentExamVO>> planList(StudentExamVO vo, @ApiIgnore Page page) {
        vo = Optional.ofNullable(vo).orElse(new StudentExamVO())
                .setUserId((Long) AuthInfoUtil.getUserId());
        return Result.ok(this.stuExamService.planList(vo, page));
    }

    @GetMapping("end-list")
    @ApiOperation("已结束考试列表")
    @ApiVersion
    @ApiImplicitParams({@ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int")
            , @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<IPage<StudentExamVO>> endList(StudentExamVO vo, @ApiIgnore Page page) {
        vo = Optional.ofNullable(vo).orElse(new StudentExamVO())
                .setUserId((Long) AuthInfoUtil.getUserId());
        return Result.ok(this.stuExamService.endList(vo, page));
    }

    @GetMapping("start/{examId}")
    @ApiOperation("开始考试")
    @ApiVersion
    @BizLog(subTypeClass = StuExamSubTypeEnum.class, subType = "STU_EXAM", action = "开始考试", detailSpEL = "'试卷名称:' + #result?.data?.examName + ',试卷id:' +#result?.data?.examId")
    public Result<StudentExamDetailVO> startExam(@ApiIgnore HttpServletRequest request, @ApiParam("试卷id") @PathVariable("examId") Long examId) {
        return Result.ok(this.stuExamService.startExam(ServletUtil.getClientIP(request), examId, (Long) AuthInfoUtil.getUserId()));
    }

    @GetMapping("continue/{examId}")
    @ApiOperation("继续考试")
    @ApiVersion
    @BizLog(subTypeClass = StuExamSubTypeEnum.class, subType = "STU_EXAM", action = "继续考试", detailSpEL = "'试卷名称:' + #result?.data?.examName + ',试卷id:' +#result?.data?.examId")
    public Result<StudentExamDetailVO> continueExam(@ApiIgnore HttpServletRequest request, @ApiParam("试卷id") @PathVariable("examId") Long examId) {
        return Result.ok(this.stuExamService.continueExam(ServletUtil.getClientIP(request), examId, (Long) AuthInfoUtil.getUserId()));
    }

    @GetMapping("exam-result/{examId}")
    @ApiOperation("考试结果")
    @ApiVersion
    public Result<StudentExamDetailVO> examResult(@ApiParam("试卷id") @PathVariable("examId") Long examId) {
        return Result.ok(this.stuExamService.examResult(examId, (Long) AuthInfoUtil.getUserId()));
    }

    @PostMapping("submit_answer")
    @ApiOperation("提交答案")
    @ApiVersion
    public Result submitAnswer(@RequestBody @Valid SubmitAnswerVO vo) {
        vo = Optional.ofNullable(vo).orElse(new SubmitAnswerVO())
                .setStuId((Long) AuthInfoUtil.getUserId());
        this.stuExamService.submitAnswer(vo);
        return Result.ok();
    }

    @PostMapping("submit_exam")
    @ApiOperation("交卷")
    @ApiVersion
    @BizLog(subTypeClass = StuExamSubTypeEnum.class, subType = "STU_EXAM", action = "考生交卷", detailSpEL = "'试卷名称:' + #result?.data + ';试卷id:' + #vo.examId")
    public Result<String> submitExam(@RequestBody @Valid SubmitExamVO vo) {
        vo = Optional.ofNullable(vo).orElse(new SubmitExamVO())
                .setStuId((Long) AuthInfoUtil.getUserId());
        return Result.ok(this.stuExamService.submitExam(vo));
    }

}
