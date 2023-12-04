package exam.bus.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.bus.IScoreService;
import exam.common.enums.ExamStatus;
import exam.common.enums.ExamStuStatusEnum;
import exam.common.vo.ExamStatisticeQueryVO;
import exam.common.vo.ExamStatisticeTotalVO;
import exam.common.vo.ScoreQueryVO;
import exam.common.vo.ScoreVO;
import exam.common.vo.StuScoreVO;
import exam.dao.entity.ExamEntity;
import exam.dao.entity.ExamStudentEntity;
import exam.dao.mapper.ExamMapper;
import exam.dao.mapper.ExamRecordMapper;
import exam.dao.mapper.ExamStudentMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ssm.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class ScoreServiceImpl implements IScoreService {

    private ExamMapper examMapper;

    private ExamStudentMapper examStudentMapper;

    private ExamRecordMapper examRecordMapper;

    /**
     * 成绩管理-分页
     */
    @Override
    public IPage<ScoreVO> pageList(Page page, ScoreQueryVO vo) {

        LambdaQueryWrapper<ExamEntity> query = new LambdaQueryWrapper<ExamEntity>().eq(ExamEntity::getStatus, ExamStatus.END);

        query.like(CharSequenceUtil.isNotBlank(vo.getExamName()), ExamEntity::getName, vo.getExamName())
                .like(CharSequenceUtil.isNotBlank(vo.getCreateBy()), exam.dao.entity.BaseEntity::getCreateBy, vo.getCreateBy())
                .ge(vo.getStartTime() != null, ExamEntity::getEndTime, vo.getStartTime())
                .le(vo.getEndTime() != null, ExamEntity::getEndTime, vo.getEndTime());

        query.orderByDesc(BaseEntity::getUpdateTime);

        IPage<ExamEntity> pageList = this.examMapper.selectPage(page, query);

        Page<ScoreVO> result = new Page<>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal());
        if (CollUtil.isEmpty(pageList.getRecords())) {
            return result;
        }
        List<ScoreVO> list = pageList.getRecords().stream().map(exam -> {
            ScoreVO score = this.getStuInfoByExamId(exam.getId());
            return score.setExamId(exam.getId())
                    .setExamName(exam.getName())
                    .setExamScore(exam.getScore())
                    .setPassScore(exam.getPassScore())
                    .setCreateBy(exam.getCreateBy())
                    .setStartTime(exam.getStartTime())
                    .setEndTime(exam.getEndTime());
        }).collect(Collectors.toList());
        return result.setRecords(list);
    }

    private ScoreVO getStuInfoByExamId(Long examId) {
        List<ExamStudentEntity> list = this.examStudentMapper.selectList(new LambdaQueryWrapper<ExamStudentEntity>().eq(ExamStudentEntity::getExamId, examId));
        ScoreVO result = new ScoreVO();
        if (CollUtil.isEmpty(list)) {
            return result;
        }
        result.setPlanStuNum(list.size());
        List<ExamStudentEntity> collect = list.stream().filter(t -> t.getStatus() == ExamStuStatusEnum.MISS_EXAM || t.getStatus() == ExamStuStatusEnum.COMPLETE || t.getStatus() == ExamStuStatusEnum.EXAM_MARKING).collect(Collectors.toList());
        // 统计特殊情况人员
        statisticsSpecialStatus(collect, result);

        ArrayList<ExamStudentEntity> copyList = new ArrayList<>(list);
        copyList.removeAll(collect);
        result.setActualStuNum(copyList.size());
        // 已有结果成绩统计
        statisticsPassAndNoPass(copyList, result);
        return result;
    }

    /**
     * 统计缺考，完成和阅卷中的人
     */
    private void statisticsSpecialStatus(List<ExamStudentEntity> list, ScoreVO vo) {
        list.forEach(t -> {
            switch (t.getStatus()) {
                case MISS_EXAM:
                    vo.setMissStuNum(vo.getMissStuNum() + 1);
                    vo.setNotPassStuNum(vo.getNotPassStuNum() + 1);
                    break;
                case COMPLETE:
                    vo.setWaitMarkingStuNum(vo.getWaitMarkingStuNum() + 1);
                    break;
                case EXAM_MARKING:
                    vo.setMarkingStuNum(vo.getMarkingStuNum() + 1);
                    break;
                default:
                    break;
            }
        });

    }

    /**
     * 统计考试中已通过和未通过的人
     */
    private void statisticsPassAndNoPass(List<ExamStudentEntity> list, ScoreVO vo) {
        BigDecimal sumScore = BigDecimal.ZERO;

        for (ExamStudentEntity entity : list) {
            if (entity.getTotalScore() != null) {
                //最高分
                vo.setMaxScore(entity.getTotalScore().compareTo(vo.getMaxScore()) > 0 ? entity.getTotalScore() : vo.getMaxScore());
                //最低分
                vo.setMinScore(entity.getTotalScore().compareTo(vo.getMinScore()) < 0 ? entity.getTotalScore() : vo.getMinScore());
                //计算该试卷的所有参考人分数
                sumScore = sumScore.add(entity.getTotalScore());
            }

            if (entity.getStatus() == ExamStuStatusEnum.PASS) {
                vo.setPassStuNum(vo.getPassStuNum() + 1);
            } else if (entity.getStatus() == ExamStuStatusEnum.NOT_PASS) {
                vo.setNotPassStuNum(vo.getNotPassStuNum() + 1);
            }
        }
        BigDecimal avgScore = sumScore;
        if (vo.getActualStuNum() > 0) {
            avgScore = NumberUtil.div(sumScore, vo.getActualStuNum(), 1);
        }
        vo.setAvgScore(avgScore);
    }

    /**
     * 考试通过详情
     *
     * @param page
     * @param examId
     * @return
     */
    @Override
    public IPage<StuScoreVO> pass(Page page, Long examId) {
        return this.examStudentMapper.findByExamIdAndStatus(CollUtil.newHashSet(ExamStuStatusEnum.PASS.getCode()), examId, page);
    }

    /**
     * 考试未通过详情
     *
     * @param page
     * @param examId
     * @return
     */
    @Override
    public IPage<StuScoreVO> notPass(Page page, Long examId) {
        return this.examStudentMapper.findByExamIdAndStatus(CollUtil.newHashSet(ExamStuStatusEnum.NOT_PASS.getCode(), ExamStuStatusEnum.MISS_EXAM.getCode()), examId, page);
    }

    @Override
    public IPage<ExamStatisticeTotalVO> statisticsExam(ExamStatisticeQueryVO vo, Page page) {
        // 默认排序方式
        if (StringUtils.isEmpty(vo.getOrder())) {
            vo.setSortName("failed");
            vo.setSortType("0");
        } else {
            String[] sortArray = vo.getOrder().split(":");
            vo.setSortName(sortArray[0]);
            vo.setSortType(sortArray[1]);
        }
        return examRecordMapper.statisticeTotal(vo, page);
    }
}
