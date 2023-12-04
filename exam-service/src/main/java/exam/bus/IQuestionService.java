package exam.bus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import exam.common.vo.*;
import exam.dao.entity.QuestionEntity;
import org.springframework.web.multipart.MultipartFile;
import ssm.common.entity.ComboBox;

import java.util.List;
import java.util.Set;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/16 10:34
 */
public interface IQuestionService extends IService<QuestionEntity> {

    /**
     * 批量录入题库
     *
     * @param list
     */
    void batchAdd(List<QuestionVO> list);

    /**
     * 新增题目
     *
     * @param vo
     */
    String add(QuestionVO vo);

    /**
     * 上传图片
     * @param file
     * @return
     */
    String uploadImage(MultipartFile file);

    /**
     * 获取服务路径
     *
     * @return
     */
    String getServerPath();

    /**
     * 修改题目
     *
     * @param vo
     */
    String update(QuestionVO vo);

    /**
     * 修改题目状态
     *
     * @param vo
     */
    void changeStatus(StatusUpdateVO vo);

    /**
     * 删除题目
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 批量删除题目
     *
     * @param ids
     */
    void delete(Set<Long> ids);

    /**
     * 获取问题类型列表
     *
     * @return
     */
    List<ComboBox> comboBoxQuestionType();

    /**
     * 获取问题难易程度列表
     * @return
     */
    List<ComboBox> comboBoxQuestionDifficulty();

    /**
     * 统计题库中各问题类型和难度的总数量
     *
     * @param questionCategoryId
     * @param type
     * @param difficulty
     * @return
     */
    Integer countNum(Long questionCategoryId, QuestionTypeEnum type, QuestionDifficultyEnum difficulty);

    /**
     * 统计类目id下启用题目数量
     *
     * @param questionCategoryId
     * @return
     */
    List<QuestionTypeCountVO> countQuestion(Long questionCategoryId);

    /**
     * 根据id查询题目内容
     *
     * @param id
     * @return
     */
    QuestionVO findById(Long id);

    /**
     * 条件分页查询题目内容 (通过xml进行表的关联查询)
     *
     * @param vo
     * @param page
     * @return
     */
    Page<QuestionResultVO> pageListByMapper(QuestionQueryVO vo, Page page);

    /**
     * 检查是否存在相同题干
     * @param title 题干
     * @return
     */
    Boolean checkDuplicate(String title);

    /**
     * 根据问题类比分类统计一级子节点下所有数据量
     */
    List<QuestionCountByCategory> questionCountByCategory();

}
