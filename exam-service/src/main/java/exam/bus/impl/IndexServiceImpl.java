package exam.bus.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IExamService;
import exam.bus.IIndexService;
import exam.bus.IQuestionService;
import exam.bus.IUserService;
import exam.common.enums.ExamStatus;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.vo.FallibleStatisticsVO;
import exam.common.vo.QuestionCountByCategory;
import exam.common.vo.RecentExamResultVO;
import exam.dao.entity.ExamEntity;
import exam.dao.mapper.AnswerStatisticsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0.0
 * @date: 2022/7/14 9:15
 * @author: yangbo
 */
@Service
@AllArgsConstructor
@Slf4j
public class IndexServiceImpl implements IIndexService {

    private IQuestionService questionService;

    private IExamService examService;

    private AnswerStatisticsMapper answerStatisticsMapper;

    private IUserService userService;


    @Override
    public Integer totalQuestion() {
        return questionService.count();
    }

    @Override
    public Integer totalExam() {
        return examService.count();
    }

    @Override
    public Integer statisticsByStatus(ExamStatus status) {
        return examService.count(new LambdaQueryWrapper<ExamEntity>().eq(ExamEntity::getStatus, status));
    }

    @Override
    public List<QuestionCountByCategory> questionCountByCategory() {
        return questionService.questionCountByCategory();
    }

    @Override
    public List<RecentExamResultVO> recentExamStatistics() {
        return examService.recentExamStatistics();
    }

    @Override
    public Page<FallibleStatisticsVO> fallibleStatistics(QuestionDifficultyEnum difficult, Page page) {
        Page<FallibleStatisticsVO> voPage = answerStatisticsMapper.fallibleStatistics(difficult.getCode(), page);
        voPage.getRecords().forEach(FallibleStatisticsVO::failedCompute);
        return voPage;
    }

    @Override
    public Integer totalPerson() {
        return userService.count();
    }

}
