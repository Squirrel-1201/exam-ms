package exam.bus.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IPracticeQuestionService;
import exam.bus.IPracticeService;
import exam.bus.util.ExamUtil;
import exam.common.enums.EnableEnum;
import exam.common.enums.PracticePaperStatus;
import exam.common.enums.PracticeStatus;
import exam.common.errorcode.CommonError;
import exam.common.errorcode.ExamError;
import exam.common.errorcode.PracticeError;
import exam.common.vo.PracticePaperQueryVO;
import exam.common.vo.PracticeQueryVO;
import exam.common.vo.PracticeQuestionVO;
import exam.common.vo.PracticeVO;
import exam.common.vo.PracticedPaperVO;
import exam.dao.entity.PracticeEntity;
import exam.dao.entity.PracticePaperEntity;
import exam.dao.entity.PracticeQuestionEntity;
import exam.dao.entity.PracticeRecordEntity;
import exam.dao.entity.PracticeRecordOptionEntity;
import exam.dao.entity.PracticeStudentEntity;
import exam.dao.entity.QuestionCategoryEntity;
import exam.dao.entity.QuestionEntity;
import exam.dao.mapper.PracticeMapper;
import exam.dao.mapper.PracticePaperMapper;
import exam.dao.mapper.PracticeRecordMapper;
import exam.dao.mapper.PracticeRecordOptionMapper;
import exam.dao.mapper.PracticeStudentMapper;
import exam.dao.mapper.QuestionCategoryMapper;
import exam.dao.mapper.QuestionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssm.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0.0
 * @date: 2022/7/13 10:35
 * @author: yangbo
 */
@Service
@AllArgsConstructor
@Slf4j
public class PracticeServiceImpl extends ServiceImpl<PracticeMapper, PracticeEntity> implements IPracticeService {

    private QuestionCategoryMapper categoryMapper;

    private QuestionMapper questionMapper;

    private IPracticeQuestionService practiceQuestionService;

    private PracticePaperMapper practicePaperMapper;

    private PracticeRecordMapper practiceRecordMapper;

    private PracticeRecordOptionMapper practiceRecordOptionMapper;

    private PracticeStudentMapper practiceStudentMapper;

    @Override
    @Transactional
    public void add(PracticeVO vo) {
        vo.trimName();
        checkPracticeVO(vo);
        PracticeEntity practice = new PracticeEntity();
        BeanUtils.copyProperties(vo, practice);
        practice.setStatus(PracticeStatus.UN_PUBLISH);
        save(practice);
        savePracticeQuestion(practice.getId(), vo.getPracticeQuestions());
    }

    @Override
    @Transactional
    public void update(PracticeVO vo) {
        CommonError.NOT_NULL.notNull(vo.getId(), "试卷id");
        checkPracticeVO(vo);
        PracticeEntity practice = getById(vo.getId());
        PracticeError.PRACTICE_NOT_FIND.notNull(practice);
        PracticeError.PRACTICE_PUBLISH_FAIL.isTrue(practice.getStatus() == PracticeStatus.UN_PUBLISH, practice.getStatus().getDesc());
        vo.trimName();
        BeanUtils.copyProperties(vo, practice);
        practice.setStatus(PracticeStatus.UN_PUBLISH);
        updateById(practice);
        practiceQuestionService.remove(new LambdaQueryWrapper<PracticeQuestionEntity>().eq(PracticeQuestionEntity::getPracticeId, practice.getId()));
        savePracticeQuestion(practice.getId(), vo.getPracticeQuestions());
    }

    @Override
    @Transactional
    public void delete(Set<Long> ids) {
        CommonError.NOT_NULL.notEmpty(ids, "试卷id");
        practiceQuestionService.remove(new LambdaQueryWrapper<PracticeQuestionEntity>().in(PracticeQuestionEntity::getPracticeId, ids));
        practicePaperMapper.delete(new LambdaQueryWrapper<PracticePaperEntity>().in(PracticePaperEntity::getPracticeId, ids));
        practiceRecordMapper.delete(new LambdaQueryWrapper<PracticeRecordEntity>().in(PracticeRecordEntity::getPracticeId, ids));
        practiceRecordOptionMapper.delete(new LambdaQueryWrapper<PracticeRecordOptionEntity>().in(PracticeRecordOptionEntity::getPracticeId, ids));
        practiceStudentMapper.delete(new LambdaQueryWrapper<PracticeStudentEntity>().in(PracticeStudentEntity::getPracticeId, ids));

        removeByIds(ids);
    }

    @Override
    public Page<PracticeEntity> pageList(PracticeQueryVO vo, Page page) {
        LambdaQueryWrapper<PracticeEntity> queryWrapper = new LambdaQueryWrapper<PracticeEntity>()
                .like(vo.getName() != null, PracticeEntity::getName, vo.getName())
                .orderByDesc(PracticeEntity::getUpdateTime);
        return page(page, queryWrapper);
    }

    @Override
    @Transactional
    public String endPractice(Long practiceId) {
        PracticeEntity practice = getById(practiceId);
        PracticeError.PRACTICE_NOT_FIND.notNull(practice);
        PracticeError.PRACTICE_STATUS_ERROR.isTrue(practice.getStatus() == PracticeStatus.PUBLISH, practice.getStatus().getDesc());
        baseMapper.updateById(practice.setStatus(PracticeStatus.END));
        practicePaperMapper.update(null, new LambdaUpdateWrapper<PracticePaperEntity>()
                .set(PracticePaperEntity::getStatus, PracticePaperStatus.PRACTICE_FINISH)
                .eq(PracticePaperEntity::getPracticeId, practice.getId()));
        return practice.getName();
    }

    @Override
    public PracticeVO findPracticeInfo(Long practiceId) {
        PracticeEntity practice = getById(practiceId);
        PracticeError.PRACTICE_NOT_FIND.notNull(practice);
        PracticeVO result = new PracticeVO();
        BeanUtil.copyProperties(practice, result);

        List<PracticeQuestionEntity> praQuestionList = practiceQuestionService.findByPracticeId(practiceId);
        if (CollUtil.isEmpty(praQuestionList)) {
            return result;
        }

        List<PracticeQuestionVO> collect = praQuestionList.stream().map(t -> {
            PracticeQuestionVO temp = new PracticeQuestionVO();
            BeanUtil.copyProperties(t, temp);
            return temp;
        }).collect(Collectors.toList());
        result.setPracticeQuestions(collect);
        return result;
    }

    @Override
    public Page<PracticedPaperVO> getPracticedList(PracticePaperQueryVO vo, Page page) {
        return practicePaperMapper.findPracticedList(vo, page);
    }

    @Override
    public String publishPractice(Long practiceId) {
        PracticeEntity practice = baseMapper.selectById(practiceId);
        PracticeError.PRACTICE_NOT_FIND.notNull(practice);
        PracticeError.PRACTICE_STATUS_ERROR.isTrue(practice.getStatus() == PracticeStatus.UN_PUBLISH, practice.getStatus().getDesc());
        PracticeError.NOW_TIME_LE_END_TIME_ERROR.isTrue(LocalDateTime.now().plusMinutes(practice.getLastTime()).isBefore(practice.getEndTime()));
        int count = practiceStudentMapper.selectCount(new LambdaQueryWrapper<PracticeStudentEntity>().eq(PracticeStudentEntity::getPracticeId, practiceId));
        PracticeError.EXAM_PUBLISH_NO_STUDENT.isTrue(count > 0);

        updateById(practice.setStatus(PracticeStatus.PUBLISH));

        return practice.getName();
    }

    private void savePracticeQuestion(Long practiceId, List<PracticeQuestionVO> practiceVOList) {
        Map<Long, String> categoryMap = getPracticeQuestionList(practiceVOList);
        Map<String, List<QuestionEntity>> questionMap = getQuestionMap(categoryMap.keySet());

        List<PracticeQuestionEntity> questionList = practiceVOList.stream().map(t -> convertToPractcieQuestion(practiceId, t, questionMap, categoryMap)).collect(Collectors.toList());
        practiceQuestionService.saveBatch(questionList);

    }

    private PracticeQuestionEntity convertToPractcieQuestion(Long practiceId, PracticeQuestionVO vo, Map<String, List<QuestionEntity>> questionMap, Map<Long, String> categoryMap) {
        ExamError.QUESTION_NUM_ERROR.isTrue(vo.getNum() > 0);
        ExamError.QUESTION_SCORE_ERROR.isTrue(NumberUtil.isGreater(vo.getScore(), BigDecimal.ZERO));
        String key = ExamUtil.generateQuestionKey(vo.getQuestionCategoryId(), vo.getType(), vo.getDifficulty());
        ExamError.QUESTION_NUM_TOO_LESS.isTrue(questionMap.containsKey(key) && questionMap.get(key).size() >= vo.getNum(), categoryMap.get(vo.getQuestionCategoryId()), vo.getType().getDesc(), vo.getDifficulty().getDesc());

        return new PracticeQuestionEntity()
                .setPracticeId(practiceId)
                .setType(vo.getType())
                .setScore(vo.getScore())
                .setNum(vo.getNum())
                .setDifficulty(vo.getDifficulty())
                .setQuestionCategoryId(vo.getQuestionCategoryId());
    }

    private Map<String, List<QuestionEntity>> getQuestionMap(Set<Long> categoryIds) {
        List<QuestionEntity> list = this.questionMapper.selectList(new LambdaQueryWrapper<QuestionEntity>()
                .eq(QuestionEntity::getStatus, EnableEnum.ENABLE)
                .in(QuestionEntity::getQuestionCategoryId, categoryIds));
        return ExamUtil.getQuestionMap(list);
    }

    private Map<Long, String> getPracticeQuestionList(List<PracticeQuestionVO> practiceVOList) {
        Set<String> allType = new HashSet<>();
        Set<Long> categoryIds = new HashSet<>();
        practiceVOList.forEach(v -> {
            allType.add(ExamUtil.generateQuestionKey(v.getQuestionCategoryId(), v.getType(), v.getDifficulty()));
            categoryIds.add(v.getQuestionCategoryId());
        });
        PracticeError.PRACTICE_CATEGORY_REPETITION.isTrue(practiceVOList.size() == allType.size());
        List<QuestionCategoryEntity> categoryList = this.categoryMapper.selectList(new LambdaQueryWrapper<QuestionCategoryEntity>()
                .eq(QuestionCategoryEntity::getStatus, EnableEnum.ENABLE)
                .in(BaseEntity::getId, categoryIds));
        PracticeError.CATEGORY_ERROR.isTrue(categoryList.size() == categoryIds.size());
        return categoryList.stream().collect(Collectors.toMap(BaseEntity::getId, QuestionCategoryEntity::getName));
    }

    private void checkPracticeVO(PracticeVO vo) {
        //总分数必须大于等于通过分数
        PracticeError.PRACTICE_SCORE_GE_PASS_SCORE.isTrue(NumberUtil.isGreaterOrEqual(vo.getScore(), vo.getPassScore()));
        //练习考试日期必须大于当前时间(允许5分钟误差)
        PracticeError.PRACTICE_START_TIME_ERROR.isTrue(vo.getStartTime().isAfter(LocalDateTime.now().minusMinutes(5)));
        //练习截止日期必须大于考试开始日期
        PracticeError.PRACTICE_END_TIME_ERROR.isTrue(vo.getEndTime().isAfter(vo.getStartTime()));
        //考试开始时间+考试持续时间必须小于考试结束时间
        PracticeError.PRACTICE_TOTAL_TIME_ERROR.isTrue(vo.getStartTime().plusMinutes(vo.getLastTime()).isBefore(vo.getEndTime()));
        LambdaQueryWrapper<PracticeEntity> wrapper = new LambdaQueryWrapper<PracticeEntity>().eq(PracticeEntity::getName, vo.getName());
        if (Objects.nonNull(vo.getId())) {
            wrapper.ne(PracticeEntity::getId, vo.getId());
        }
        int count = count(wrapper);
        PracticeError.PRACTICE_NAME_HAD_EXIST.isTrue(count < 1);
    }
}
