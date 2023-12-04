package exam.web.controller.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IScoreService;
import exam.bus.IStuExamService;
import exam.bus.IStuPracticeService;
import exam.common.vo.ExamStatisticeQueryVO;
import exam.common.vo.ExamStatisticeTotalVO;
import exam.common.vo.PracticePaperDetailVO;
import exam.common.vo.ScoreQueryVO;
import exam.common.vo.ScoreVO;
import exam.common.vo.StuScoreVO;
import exam.common.vo.StudentExamDetailVO;
import io.swagger.annotations.*;
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

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/score")
@Api(tags = "成绩管理")
public class ScoreController {

    private IScoreService scoreService;

    private IStuExamService stuExamService;

    private IStuPracticeService stuPracticeService;

    @GetMapping("list")
    @ApiOperation("分页查询")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<IPage<ScoreVO>> pageList(@ApiIgnore Page page, ScoreQueryVO vo) {
        return Result.ok(this.scoreService.pageList(page, vo));
    }

    @PostMapping("statistics-exam")
    @ApiOperation("考试试题错误详情统计")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<IPage<ExamStatisticeTotalVO>> statisticsExam(@RequestBody @Valid ExamStatisticeQueryVO vo, @ApiIgnore Page page) {
        return Result.ok(scoreService.statisticsExam(vo, page));
    }

    @GetMapping("pass/{examId}")
    @ApiOperation("考试通过人数详情")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<IPage<StuScoreVO>> pass(@ApiIgnore Page page, @ApiParam("试卷id") @PathVariable("examId") Long examId) {
        return Result.ok(this.scoreService.pass(page, examId));
    }

    @GetMapping("not-pass/{examId}")
    @ApiOperation("考试未通过人数详情")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<IPage<StuScoreVO>> notPass(@ApiIgnore Page page, @ApiParam("试卷id") @PathVariable("examId") Long examId) {
        return Result.ok(this.scoreService.notPass(page, examId));
    }

    @GetMapping("{examId}/{stuId}")
    @ApiOperation("考试详情")
    @ApiVersion
    public Result<StudentExamDetailVO> examResultDetail(@ApiParam("试卷id") @PathVariable("examId") Long examId, @ApiParam("考生id") @PathVariable("stuId") Long stuId) {
        return Result.ok(this.stuExamService.examResult(examId, stuId));
    }

    @GetMapping("practice/{paperId}")
    @ApiOperation("练习试卷详情")
    @ApiVersion
    public Result<PracticePaperDetailVO> practicePaperResult(@ApiParam("练习试卷id") @PathVariable("paperId") Long paperId) {
        return Result.ok(stuPracticeService.practicePaperResult(paperId));
    }

}
