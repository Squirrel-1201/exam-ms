package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.common.vo.StudentPracticeVO;
import exam.dao.entity.PracticeStudentEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @version 1.0.0
 * @date: 2022/7/13 16:20
 * @author: yangbo
 */
public interface PracticeStudentMapper extends BaseMapper<PracticeStudentEntity> {

    IPage<StudentPracticeVO> selectPracticeList(@Param("vo") StudentPracticeVO vo, Page page);
}
