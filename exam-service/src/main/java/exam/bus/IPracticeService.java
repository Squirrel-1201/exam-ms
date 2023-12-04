package exam.bus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.vo.PracticePaperQueryVO;
import exam.common.vo.PracticeQueryVO;
import exam.common.vo.PracticeVO;
import exam.common.vo.PracticedPaperVO;
import exam.dao.entity.PracticeEntity;

import java.util.Set;

/**
 * @version 1.0.0
 * @date: 2022/7/13 10:34
 * @author: yangbo
 */
public interface IPracticeService extends IService<PracticeEntity> {

    /**
     * 添加练习
     */
    void add(PracticeVO vo);

    /**
     * 修改练习
     */
    void update(PracticeVO vo);

    /**
     * 批量删除练习
     */
    void delete(Set<Long> ids);

    /**
     * 分页查询
     */
    Page<PracticeEntity> pageList(PracticeQueryVO vo, Page page);

    /**
     * 结束练习
     */
    String endPractice(Long practiceId);

    /**
     * 根据id查找练习详细信息
     */
    PracticeVO findPracticeInfo(Long practiceId);

    /**
     * 分页查询考试试卷列表
     */
    Page<PracticedPaperVO> getPracticedList(PracticePaperQueryVO vo, Page page);

    /**
     * 发布练习
     */
    String publishPractice(Long practiceId);
}
