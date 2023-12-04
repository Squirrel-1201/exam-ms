package exam.bus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IExamQuestionService;
import exam.dao.entity.ExamQuestionEntity;
import exam.dao.mapper.ExamQuestionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/18 10:56
 */
@Service
@Slf4j
@AllArgsConstructor
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestionMapper, ExamQuestionEntity> implements IExamQuestionService {

    @Override
    public List<ExamQuestionEntity> findExamQuestionByExamId(Long examId) {
        return this.baseMapper.selectList(new QueryWrapper<ExamQuestionEntity>().eq("exam_id", examId));
    }
}
