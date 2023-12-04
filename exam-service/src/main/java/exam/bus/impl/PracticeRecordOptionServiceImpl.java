package exam.bus.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IPracticeRecordOptionService;
import exam.dao.entity.PracticeRecordOptionEntity;
import exam.dao.mapper.PracticeRecordOptionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @version 1.0.0
 * @date: 2022/7/18 13:44
 * @author: yangbo
 */
@Service
@AllArgsConstructor
@Slf4j
public class PracticeRecordOptionServiceImpl extends ServiceImpl<PracticeRecordOptionMapper, PracticeRecordOptionEntity> implements IPracticeRecordOptionService {
}
