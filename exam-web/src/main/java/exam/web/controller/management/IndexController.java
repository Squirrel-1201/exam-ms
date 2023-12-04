package exam.web.controller.management;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IIndexService;
import exam.common.enums.ExamStatus;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.vo.FallibleStatisticsVO;
import exam.common.vo.QuestionCountByCategory;
import exam.common.vo.RecentExamResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import ssm.common.annotation.ApiVersion;
import ssm.common.model.Result;

import java.util.List;
import java.util.Objects;

/**
 * @version 1.0.0
 * @date: 2022/7/14 9:11
 * @author: yangbo
 */
@RestController
@RequestMapping("admin/index")
@Api(tags = "首页")
@AllArgsConstructor
public class IndexController {

    private IIndexService indexService;

    @GetMapping("total-question")
    @ApiOperation("试题总数")
    @ApiVersion
    public Result<Integer> totalQuestion() {
        return Result.ok(indexService.totalQuestion());
    }

    @GetMapping("total-exam")
    @ApiOperation("考试总数")
    @ApiVersion
    public Result<Integer> totalExam() {
        return Result.ok(indexService.totalExam());
    }

    @GetMapping("unpublish-exam")
    @ApiOperation("未发布试卷数")
    @ApiVersion
    public Result<Integer> unpublishExam() {
        return Result.ok(indexService.statisticsByStatus(ExamStatus.UN_PUBLISHED));
    }

    @GetMapping("publish-exam")
    @ApiOperation("已发布试卷数")
    @ApiVersion
    public Result<Integer> publishedExam() {
        return Result.ok(indexService.statisticsByStatus(ExamStatus.PUBLISHED));
    }

    @GetMapping("count-by-category")
    @ApiOperation("一级子节点下所有题目分类数量统计")
    @ApiVersion
    public Result<List<QuestionCountByCategory>> countByCategory() {
        return Result.ok(indexService.questionCountByCategory());
    }

    @GetMapping("recent-exam")
    @ApiOperation("最近5次已完成考试数据统计")
    @ApiVersion
    public Result<List<RecentExamResultVO>> recentExam() {
        return Result.ok(indexService.recentExamStatistics());
    }

    @GetMapping("fallible-question/{difficult}")
    @ApiOperation("易错题统计")
    @ApiVersion
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataType = "int")})
    public Result<Page<FallibleStatisticsVO>> fallibleQuestion(@PathVariable("difficult") QuestionDifficultyEnum difficult, @ApiIgnore Page page) {
        if (Objects.isNull(difficult)) {
            difficult = QuestionDifficultyEnum.DIFFICULTY;
        }
        return Result.ok(indexService.fallibleStatistics(difficult, page));
    }

    @GetMapping("total-person")
    @ApiOperation("所有人数统计")
    @ApiVersion
    public Result<Integer> totalPerson(){
        return Result.ok(indexService.totalPerson());
    }

}
