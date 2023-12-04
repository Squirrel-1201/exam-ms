package exam.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import exam.common.vo.FallibleStatisticsVO;
import exam.dao.entity.AnswerStatisticsEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @version 1.0.0
 * @date: 2022/7/6 15:38
 * @author: yangbo
 */
public interface AnswerStatisticsMapper extends BaseMapper<AnswerStatisticsEntity> {

    Integer selectByQustionId(@Param("questionId") Long questionId);

    void updateTotalAndFailByQustionId(@Param("questionId") Long questionId, @Param("failCount") Integer fail, @Param("total") Integer total);

    Page<FallibleStatisticsVO> fallibleStatistics(@Param("code") Integer code, Page page);

}
