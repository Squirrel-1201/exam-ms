package exam.bus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.common.enums.ExamStatus;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.vo.FallibleStatisticsVO;
import exam.common.vo.QuestionCountByCategory;
import exam.common.vo.RecentExamResultVO;

import java.util.List;

/**
 * @version 1.0.0
 * @date: 2022/7/14 9:14
 * @author: yangbo
 */
public interface IIndexService {
    /**
     * 统计问题总数
     */
    Integer totalQuestion();

    /**
     * 总的试卷数
     */
    Integer totalExam();

    /**
     * 根据试卷状态统计试卷数
     */
    Integer statisticsByStatus(ExamStatus status);

    /**
     * 问题分类统计
     */
    List<QuestionCountByCategory> questionCountByCategory();

    /**
     * 统计最近5次考试通过率
     */
    List<RecentExamResultVO> recentExamStatistics();

    /**
     * 错误率统计
     */
    Page<FallibleStatisticsVO> fallibleStatistics(QuestionDifficultyEnum difficult, Page page);

    Integer totalPerson();
}
