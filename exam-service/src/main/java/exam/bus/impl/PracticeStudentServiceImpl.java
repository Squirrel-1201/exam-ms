package exam.bus.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IPracticeService;
import exam.bus.IPracticeStudentService;
import exam.bus.IUserService;
import exam.common.enums.PracticeStatus;
import exam.common.enums.UserStatusEnum;
import exam.common.errorcode.PracticeError;
import exam.common.errorcode.UserError;
import exam.common.vo.PracticeStudentAddVO;
import exam.dao.entity.PracticeEntity;
import exam.dao.entity.PracticeStudentEntity;
import exam.dao.entity.UserEntity;
import exam.dao.mapper.PracticeStudentMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssm.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0
 * @date: 2022/7/13 17:53
 * @author: yangbo
 */
@Service
@AllArgsConstructor
@Slf4j
public class PracticeStudentServiceImpl extends ServiceImpl<PracticeStudentMapper, PracticeStudentEntity> implements IPracticeStudentService {

    private IPracticeService practiceService;

    private IUserService userService;


    @Override
    public String addPracticeStudent(PracticeStudentAddVO vo) {
        PracticeEntity practice = practiceService.getById(vo.getPracticeId());
        PracticeError.PRACTICE_NOT_FIND.notNull(practice);
        PracticeError.PRACTICE_PUBLISH_FAIL.isTrue(practice.getStatus() == PracticeStatus.UN_PUBLISH, practice.getStatus().getDesc());

        int count = this.userService.count(new LambdaQueryWrapper<UserEntity>().in(BaseEntity::getId, vo.getStuId()).ne(UserEntity::getStatus, UserStatusEnum.CANCEL));
        UserError.USER_NOT_EXIST.isTrue(count == vo.getStuId().size());

        List<PracticeStudentEntity> result = new ArrayList<>();

        vo.getStuId().forEach(t -> {
            PracticeStudentEntity temp = new PracticeStudentEntity()
                    .setStuId(t)
                    .setPracticeId(vo.getPracticeId())
                    .setUsedTimes(0);
            result.add(temp);
        });

        remove(new LambdaQueryWrapper<PracticeStudentEntity>().eq(PracticeStudentEntity::getPracticeId, vo.getPracticeId()));
        saveBatch(result);
        return practice.getName();
    }

    @Override
    public List<Long> findStudentsByPracticeId(Long practiceId) {
        return listObjs(new LambdaQueryWrapper<PracticeStudentEntity>()
                .eq(PracticeStudentEntity::getPracticeId, practiceId)
                .select(PracticeStudentEntity::getStuId), o -> Long.valueOf(o.toString()));
    }
}
