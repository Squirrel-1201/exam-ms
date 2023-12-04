package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import exam.dao.entity.ExamRecordOptionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExamRecordOptionMapper extends BaseMapper<ExamRecordOptionEntity> {


    void batchSave(@Param("list") List<ExamRecordOptionEntity> list);

    List<ExamRecordOptionEntity> findByExamIdAndStuId(@Param("examId") Long examId, @Param("stuId") Long stuId);
}
