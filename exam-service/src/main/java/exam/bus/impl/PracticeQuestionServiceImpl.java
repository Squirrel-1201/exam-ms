package exam.bus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IPracticeQuestionService;
import exam.dao.entity.PracticeQuestionEntity;
import exam.dao.mapper.PracticeQuestionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0.0
 * @date: 2022/7/13 15:02
 * @author: yangbo
 */
@Service
@AllArgsConstructor
@Slf4j
public class PracticeQuestionServiceImpl extends ServiceImpl<PracticeQuestionMapper, PracticeQuestionEntity> implements IPracticeQuestionService {
    @Override
    public List<PracticeQuestionEntity> findByPracticeId(Long practiceId) {
        return baseMapper.selectList(new QueryWrapper<PracticeQuestionEntity>().eq("practice_id", practiceId));
    }
}
