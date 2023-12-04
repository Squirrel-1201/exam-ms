package exam.bus.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IQuestionOptionService;
import exam.dao.entity.QuestionOptionEntity;
import exam.dao.mapper.QuestionOptionMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class QuestionOptionServiceImpl extends ServiceImpl<QuestionOptionMapper, QuestionOptionEntity> implements IQuestionOptionService {
}
