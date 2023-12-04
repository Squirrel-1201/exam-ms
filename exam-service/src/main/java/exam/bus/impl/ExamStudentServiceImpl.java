package exam.bus.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IExamService;
import exam.bus.IExamStudentService;
import exam.bus.IUserService;
import exam.common.errorcode.ExamError;
import exam.common.vo.ExamStudentAddVO;
import exam.common.enums.ExamStatus;
import exam.common.enums.ExamStuStatusEnum;
import exam.common.enums.UserStatusEnum;
import exam.common.errorcode.UserError;
import exam.dao.entity.ExamEntity;
import exam.dao.entity.ExamStudentEntity;
import exam.dao.entity.UserEntity;
import exam.dao.mapper.ExamStudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssm.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/25 14:05
 */
@Service
public class ExamStudentServiceImpl extends ServiceImpl<ExamStudentMapper, ExamStudentEntity> implements IExamStudentService {

    @Autowired
    private IExamService examService;
    @Autowired
    private IUserService userService;


    @Override
    public String addExamStudent(ExamStudentAddVO vo) {
        // 校验试卷
        ExamEntity exam = this.checkExam(vo.getExamId());
        // 检验考生
        this.checkExamStudent(vo.getStuId());
        // 保存考生考考试信息
        List<ExamStudentEntity> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(vo.getStuId())) {
            for (Long studentId : vo.getStuId()) {
                ExamStudentEntity entity = new ExamStudentEntity();
                entity.setStuId(studentId);
                entity.setExamId(vo.getExamId());
                // 设置考试状态为待考状态
                entity.setStatus(ExamStuStatusEnum.TO_BE_EXAM);
                list.add(entity);
            }
        }

        // 删除该试卷关联的考生
        this.remove(new LambdaQueryWrapper<ExamStudentEntity>().eq(ExamStudentEntity::getExamId,vo.getExamId()));
        this.saveBatch(list);

        return exam.getName();
    }

    @Override
    public List<Long> findStudentsByExamId(Long examId) {
        return this.listObjs(new LambdaQueryWrapper<ExamStudentEntity>().eq(ExamStudentEntity::getExamId, examId).select(ExamStudentEntity::getStuId), o -> Long.valueOf(o.toString()));
    }


    /**
     * 校验试卷
     *
     * @param id
     */
    private ExamEntity checkExam(Long id) {
        ExamEntity examEntity = this.examService.getById(id);
        ExamError.NOT_FIND_EXAM.notNull(examEntity);
        ExamError.STUDENT_CHOSE_EXAM_ERROR.isTrue(examEntity.getStatus() == ExamStatus.UN_PUBLISHED);
        return examEntity;
    }

    /**
     * 检验考生
     *
     * @param ids
     */
    private void checkExamStudent(Set<Long> ids) {
        long count = this.userService.count(new LambdaQueryWrapper<UserEntity>().in(BaseEntity::getId, ids).ne(UserEntity::getStatus, UserStatusEnum.CANCEL));
        UserError.USER_NOT_EXIST.isTrue(count == ids.size());
    }
}
