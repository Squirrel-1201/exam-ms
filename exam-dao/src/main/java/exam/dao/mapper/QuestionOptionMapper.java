package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import exam.dao.entity.QuestionOptionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface QuestionOptionMapper extends BaseMapper<QuestionOptionEntity> {

    List<QuestionOptionEntity> findByCategoryIds(@Param("categoryIds") Set<Long> categoryIds);

}
