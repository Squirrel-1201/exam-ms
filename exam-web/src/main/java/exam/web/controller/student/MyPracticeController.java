package exam.web.controller.student;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IStuPracticeService;
import exam.common.vo.FinishPracticePaperVO;
import exam.common.vo.PracticeAnswerVO;
import exam.common.vo.PracticePaperDetailVO;
import exam.common.vo.StudentPracticeVO;
import exam.common.vo.SubmitPracticePaperVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import ssm.common.annotation.ApiVersion;
import ssm.common.model.Result;
import ssm.common.util.AuthInfoUtil;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @version 1.0.0
 * @date: 2022/7/15 10:44
 * @author: yangbo
 */
@AllArgsConstructor
@RestController
@RequestMapping("stu/my-practice")
@Api(tags = "我的练习")
public class MyPracticeController {

    private IStuPracticeService stuPracticeService;

    @GetMapping("practice-list")
    @ApiOperation("练习计划列表")
    @ApiVersion
    @ApiImplicitParams({@ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int")
            , @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<IPage<StudentPracticeVO>> practiceList(StudentPracticeVO vo, @ApiIgnore Page page) {
        vo = Optional.ofNullable(vo).orElse(new StudentPracticeVO())
                .setUserId((Long) AuthInfoUtil.getUserId());
        return Result.ok(stuPracticeService.practiceList(vo, page));
    }

    @GetMapping("finish-paper-list")
    @ApiOperation("已完成试卷列表")
    @ApiVersion
    @ApiImplicitParams({@ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int")
            , @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<IPage<FinishPracticePaperVO>> queryFinishPaperList(FinishPracticePaperVO vo, @ApiIgnore Page page) {
        vo = Optional.ofNullable(vo).orElse(new FinishPracticePaperVO())
                .setUserId((Long) AuthInfoUtil.getUserId());
        return Result.ok(stuPracticeService.queryFinishPaperList(vo, page));
    }

    @GetMapping("start/{practiceId}")
    @ApiOperation("开始练习")
    @ApiVersion
    public Result<PracticePaperDetailVO> startPractice(@ApiParam("练习Id") @PathVariable("practiceId") Long practiceId) {
        return Result.ok(stuPracticeService.startPractice(practiceId, (Long) AuthInfoUtil.getUserId()));
    }

    @GetMapping("continue/{practiceId}")
    @ApiOperation("继续练习")
    @ApiVersion
    public Result<PracticePaperDetailVO> continuePractice(@ApiParam("练习Id") @PathVariable("practiceId") Long practiceId) {
        return Result.ok(stuPracticeService.continuePractice(practiceId, (Long) AuthInfoUtil.getUserId()));
    }

    @GetMapping("practice-result/{paperId}")
    @ApiOperation("练习结果")
    @ApiVersion
    public Result<PracticePaperDetailVO> practiceResult(@ApiParam("练习试卷id") @PathVariable("paperId") Long paperId) {
        return Result.ok(stuPracticeService.practicePaperResult(paperId));
    }

    @PostMapping("submit-answer")
    @ApiOperation("提交练习答案")
    @ApiVersion
    public Result<String> submitAnswer(@RequestBody @Valid PracticeAnswerVO vo) {
        stuPracticeService.submitAnswer(vo);
        return Result.ok();
    }

    @PostMapping("submit-practice-paper")
    @ApiOperation("提交练习试卷")
    @ApiVersion
    public Result<String> submitPaper(@RequestBody @Valid SubmitPracticePaperVO vo) {
        return Result.ok(stuPracticeService.submitPracticePaper(vo));
    }

}
