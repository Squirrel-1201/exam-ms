package exam.bus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.vo.*;
import exam.dao.entity.ExamEntity;
import ssm.common.entity.ComboBox;

import java.util.List;
import java.util.Set;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/18 10:43
 */
public interface IExamService extends IService<ExamEntity> {

    /**
     * 新增考试试卷
     *
     * @param vo
     */
    void add(ExamVO vo);

    /**
     * 修改考试试卷
     *
     * @param vo
     */
    void update(ExamVO vo);

    /**
     * 发布试卷
     *
     * @param id
     */
    String publishExam(Long id);

    /**
     * 批量删除试卷
     *
     * @param ids
     */
    void delete(Set<Long> ids);

    /**
     * 根据id查询试卷详情
     *
     * @param id
     * @return
     */
    ExamVO findExamInfo(Long id);

    /**
     * 分页条件查询试卷信息
     *
     * @param vo
     * @param page
     * @return
     */
    Page<ExamEntity> pageList(ExamQueryVO vo, Page page);

    /**
     * 手动结束考试
     *
     * @param examId
     */
    String endExam(Long examId);

    /**
     * 自动阅卷
     *
     * @param examId
     */
    void marking(Long examId);

    /**
     * 开始人工阅卷
     */
    void artificialMarkingExam(ArtificialMarkingVO vo);

    /**
     * 提交人工阅卷信息
     *
     * @param vo
     */
    void commitMarkingExam(SubmitMarkingResult vo);

    /**
     * 分页条件查询阅卷中的考生试卷
     * @param vo
     * @param page
     * @return
     */
    Page<ArtificialMarkExamResultVO> getMarkingExamList(ArtificialMarkQueryVO vo,Page page);

    /**
     * 分页条件查询考生试卷信息
     * @param vo
     * @param page
     * @return
     */
    Page<ArtificialMarkExamResultVO> getStudentExamList(StuExamQueryVO vo, Page page);

    /**
     * 获取考生考试状态列表
     * @return
     */
    List<ComboBox> comboBoxStudentStatus();

    /**
     * 获取试卷状态列表
     * @return
     */
    List<ComboBox> comboBoxExamStatus();

    /**
     * 自动结束试卷
     *
     * @param examId
     */
    void autoEndExamStatus(Long examId);

    /**
     * 查询未结束考试列表
     * @return
     */
    List<ExamEntity> getNotEndExamList();

    /**
     * 统计最近5次考试通过率
     */
    List<RecentExamResultVO> recentExamStatistics();
}
