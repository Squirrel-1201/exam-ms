package exam.bus.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import exam.bus.IQuestionOptionService;
import exam.bus.IQuestionService;
import exam.common.constant.Constant;
import exam.common.enums.EnableEnum;
import exam.common.enums.QuestionDifficultyEnum;
import exam.common.enums.QuestionTypeEnum;
import exam.common.enums.YNEnum;
import exam.common.errorcode.CommonError;
import exam.common.errorcode.QuestionCategoryErrorCode;
import exam.common.errorcode.QuestionErrorCode;
import exam.common.vo.*;
import exam.dao.entity.QuestionCategoryEntity;
import exam.dao.entity.QuestionEntity;
import exam.dao.entity.QuestionOptionEntity;
import exam.dao.mapper.QuestionCategoryMapper;
import exam.dao.mapper.QuestionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import ssm.common.entity.ComboBox;


import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.date.DateUtil.date;

/**
 * @author: zhoufs
 * @Description:
 * @date 2022/2/16 11:41
 */
@Service
@Slf4j
@AllArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, QuestionEntity> implements IQuestionService {

    private QuestionCategoryMapper categoryMapper;

    private IQuestionOptionService optionService;

    private static final String BASEDIR = "images";


    /**
     * 批量录入题库
     *
     * @param list
     */
    @Override
    @Transactional
    public void batchAdd(List<QuestionVO> list) {
        CommonError.NOT_NULL.notEmpty(list, "批量添加的问题");
        list.stream().forEach(q -> this.add(q.setStatus(EnableEnum.ENABLE)));
    }

    @Override
    @Transactional
    public String add(QuestionVO vo) {

        QuestionCategoryEntity category = this.categoryMapper.selectById(vo.getQuestionCategoryId());
        QuestionErrorCode.NOT_FIND_QUESTION_CATEGORY.notNull(category);

        // 校验问题选项和答案是否匹配
        this.valid(vo);

        // 保存题目
        QuestionEntity entity = new QuestionEntity();
        BeanUtils.copyProperties(vo, entity);
        entity.setId(IdWorker.getId());
        this.save(entity);

        //保存答案选项
        this.saveQuestionOption(vo.getType(), vo.getOptions(), entity.getId());

        // 返回题目类别名称记录到日志中
        return categoryMapper.selectById(entity.getQuestionCategoryId()).getName();
    }

    @Override
    public String uploadImage(MultipartFile file) {
        QuestionErrorCode.FILE_IS_EMPTY.notNull(file);

        // 获取文件后缀
        String imageSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (!".jpg,.jpeg,.gif,.png".toUpperCase().contains(imageSuffix.toUpperCase())) {
            QuestionErrorCode.IMAGE_TYPE_ERROR.assertThrow(imageSuffix);
        }

        // 获取文件大小
        log.info("上传图片文件的大小 fileSize={} KB", file.getSize() / 1024);
        if (file.getSize() > 1024 * 1024 * 10) {
            QuestionErrorCode.FILE_TOO_BIGGER.assertThrow("10M");
        }
        DateTime date = date();
        String yearAndMonth = date.year() + "-" + (date.month() + 1);
        File savePath = new File(BASEDIR + Constant.FILE_SEPARATOR + yearAndMonth);
        if (!savePath.exists()) {
            savePath.mkdirs();
        }
        String numFor16 = this.randomNumFor16();
        String fileName = numFor16 + imageSuffix;
        log.info("上传图片保存路径 savePath={} ,保存文件名fileName={}", savePath, fileName);
        // 图片文件保存全路径
        String realAccessPath = savePath + Constant.FILE_SEPARATOR + fileName;
        try {
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(realAccessPath));
        } catch (Exception e) {
            QuestionErrorCode.FILE_SAVE_FAIL.assertThrow(e.getMessage());
        }
        return Constant.FILE_SEPARATOR + realAccessPath.replace("\\", Constant.FILE_SEPARATOR);
    }

    @Override
    public String getServerPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 生产一个16位随机数
     *
     * @return
     */
    private String randomNumFor16() {
        String currentTimeMillis = System.currentTimeMillis() + "";
        String randomNum = (int) (Math.random() * 900) + 100 + "";
        return currentTimeMillis + randomNum;
    }

    private void valid(QuestionVO vo) {
        if (vo.getType() == QuestionTypeEnum.SINGLE) {
            this.validSingle(vo);
            return;
        }

        if (vo.getType() == QuestionTypeEnum.MULTIPLE) {
            this.validMultiple(vo);
            return;
        }

        if (vo.getType() == QuestionTypeEnum.JUDGE) {
            this.validJudge(vo);
            return;
        }
        if (vo.getType() == QuestionTypeEnum.ESSAY) {
            this.validEssay(vo);
            return;
        }
        QuestionErrorCode.TYPE_ERROR.assertThrow(vo.getType(), vo.getTitle(), vo.getOptions());
    }

    private void validSingle(QuestionVO vo) {
        //选项至少有两个
        QuestionErrorCode.OPTION_TOO_LESS.isTrue(CollUtil.isNotEmpty(vo.getOptions()) || vo.getOptions().size() >= 2);
        vo.setAnswer(vo.getAnswer().toUpperCase());
        this.validAnswer(vo.getOptions(), vo.getAnswer(), true);
    }


    private void validMultiple(QuestionVO vo) {
        //选项至少有两个
        QuestionErrorCode.OPTION_TOO_LESS.isTrue(CollUtil.isNotEmpty(vo.getOptions()) || vo.getOptions().size() >= 2);
        vo.setAnswer(vo.getAnswer().toUpperCase());
        this.validAnswer(vo.getOptions(), vo.getAnswer(), false);
    }


    private void validJudge(QuestionVO vo) {
        //判断题不能有选项
        QuestionErrorCode.JUDGE_NOT_OPTION.isTrue(CollUtil.isEmpty(vo.getOptions()));
        QuestionErrorCode.ANSWER_MISMATCH.isTrue(vo.getAnswer().equals("0") || vo.getAnswer().equals("1"), QuestionTypeEnum.JUDGE, vo.getOptions(), vo.getAnswer());
    }

    private void validEssay(QuestionVO vo) {
        //简答题不能有选项
        QuestionErrorCode.ESSAY_NOT_OPTION.isTrue(CollUtil.isEmpty(vo.getOptions()));
    }


    /**
     * 答案校验
     *
     * @param options  选项集合
     * @param answer   答案
     * @param isSingle 是否单选
     */
    private void validAnswer(List<String> options, String answer, boolean isSingle) {
        int end = Constant.OPTION_MIN + options.size() - 1;
        end = end > Constant.OPTION_MAX ? Constant.OPTION_MAX : end;
        if (isSingle) {
            //单选题答案只能有一个
            QuestionErrorCode.ANSWER_MISMATCH.isTrue(answer.length() == 1, QuestionTypeEnum.SINGLE, options, answer);
        } else {
            //多选题答案至少有两项
            QuestionErrorCode.ANSWER_MISMATCH.isTrue(answer.length() >= 2, QuestionTypeEnum.MULTIPLE, options, answer);
        }

        int size = CollUtil.newHashSet(answer.split(",")).size();
        answer = answer.replace(",", "");
        char[] chars = answer.toCharArray();

        //去重后验证答案长度
        QuestionErrorCode.ANSWER_LENGTH_MISMATCH.isTrue(size == chars.length, options, answer);

        for (int i = 0; i < size; i++) {
            //判断答案合法性
            QuestionErrorCode.ANSWER_ILLEGAL.isTrue(Integer.valueOf(chars[i]) >= Constant.OPTION_MIN && Integer.valueOf(chars[i]) <= end, options, answer);
        }
    }


    private void saveQuestionOption(QuestionTypeEnum type, List<String> options, Long questionId) {
        //保存题目选项
        if (type == QuestionTypeEnum.SINGLE || type == QuestionTypeEnum.MULTIPLE) {
            List<QuestionOptionEntity> list = CollUtil.newArrayList();
            if (CollUtil.isNotEmpty(options)) {
                for (String option : options) {
                    // 选择题选项内容不能为空格字符
                    QuestionErrorCode.OPTION_IS_NOT_BLANK.isTrue(CharSequenceUtil.isNotBlank(option), type, options);

                    QuestionOptionEntity optionEntity = new QuestionOptionEntity()
                            .setTitle(option)
                            .setSerialNo(String.valueOf((char) (Constant.OPTION_MIN + list.size())))
                            .setQuestionId(questionId);
                    list.add(optionEntity);
                }
                this.optionService.saveBatch(list);
            }
        }
    }

    @Override
    @Transactional
    public String update(QuestionVO vo) {
        CommonError.NOT_NULL.notNull(vo.getId());

        QuestionCategoryEntity category = this.categoryMapper.selectById(vo.getQuestionCategoryId());
        QuestionErrorCode.NOT_FIND_QUESTION_CATEGORY.notNull(category);

        QuestionEntity entity = this.getById(vo.getId());
        QuestionErrorCode.NOT_FIND_QUESTION.notNull(entity);

        // 校验问题选项和答案是否匹配
        this.valid(vo);
        // 保存修改
        BeanUtils.copyProperties(vo, entity);
        this.updateById(entity);

        //清除旧选项
        this.optionService.remove(new LambdaQueryWrapper<QuestionOptionEntity>().eq(QuestionOptionEntity::getQuestionId, entity.getId()));
        //保存答案选项
        this.saveQuestionOption(vo.getType(), vo.getOptions(), entity.getId());

        // 返回题目类别名称记录到日志中
        return this.categoryMapper.selectById(entity.getQuestionCategoryId()).getName();
    }

    @Override
    public void changeStatus(StatusUpdateVO vo) {
        QuestionEntity entity = this.getById(vo.getId());
        QuestionErrorCode.NOT_FIND_QUESTION.notNull(entity);
        entity.setStatus(vo.getStatus());
        this.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        QuestionEntity entity = this.getById(id);
        QuestionErrorCode.NOT_FIND_QUESTION.notNull(entity);
        // 删除题库问题
        this.removeById(id);
        // 删除问题对应的选项
        this.optionService.remove(new LambdaQueryWrapper<QuestionOptionEntity>().eq(QuestionOptionEntity::getQuestionId, id));
    }

    @Override
    @Transactional
    public void delete(Set<Long> ids) {
        CommonError.NOT_NULL.notEmpty(ids, "id");
        // 批量删除题库问题
        this.removeByIds(ids);
        // 批量删除问题对应的选项
        this.optionService.remove(new LambdaQueryWrapper<QuestionOptionEntity>().in(QuestionOptionEntity::getQuestionId, ids));
    }

    @Override
    public List<ComboBox> comboBoxQuestionType() {
        return CollUtil.newArrayList(QuestionTypeEnum.values()).stream().map(s -> {
            ComboBox box = new ComboBox();
            box.setLabel(s.getDesc());
            box.setValue(String.valueOf(s.getCode()));
            return box;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ComboBox> comboBoxQuestionDifficulty() {
        return CollUtil.newArrayList(QuestionDifficultyEnum.values()).stream().map(s -> {
            ComboBox box = new ComboBox();
            box.setLabel(s.getDesc());
            box.setValue(String.valueOf(s.getCode()));
            return box;
        }).collect(Collectors.toList());
    }

    @Override
    public Integer countNum(Long questionCategoryId, QuestionTypeEnum type, QuestionDifficultyEnum difficulty) {
        return this.baseMapper.countQuestionNumByDifficultAndType(questionCategoryId, type, difficulty);
    }

    @Override
    public List<QuestionTypeCountVO> countQuestion(Long questionCategoryId) {
        // 校验问题类目id
        QuestionCategoryEntity entity = this.categoryMapper.selectById(questionCategoryId);
        QuestionCategoryErrorCode.NOT_FIND_QUESTION_CATEGORY.notNull(entity);
        QuestionCategoryErrorCode.CATEGORY_DISABLE.isTrue(entity.getStatus().equals(EnableEnum.ENABLE));
        // 创建返回结果集合
        List<QuestionTypeCountVO> result = CollUtil.newArrayList();
        //问题类型
        Arrays.stream(QuestionTypeEnum.values()).forEach(type -> {

            QuestionTypeCountVO vo = new QuestionTypeCountVO().setQuestionType(type).setQuestionCategoryId(questionCategoryId);
            List<QuestionEnableCountVO> questionList = CollUtil.newArrayList();
            //问题难易程度
            Arrays.stream(QuestionDifficultyEnum.values()).forEach(difficulty -> {
                Integer count = this.countNum(questionCategoryId, type, difficulty);
                vo.setTotalNum(vo.getTotalNum() + count);
                QuestionEnableCountVO countVO = new QuestionEnableCountVO().setQuestionNum(count).setDifficulty(difficulty).setType(type).setQuestionCategoryId(questionCategoryId);
                questionList.add(countVO);
            });
            //问题难易程度集合
            vo.setDifficultyCount(questionList);
            result.add(vo);
        });
        return result;
    }

    @Override
    public QuestionVO findById(Long id) {
        QuestionEntity entity = this.getById(id);
        QuestionErrorCode.NOT_FIND_QUESTION.notNull(entity);
        LambdaQueryWrapper<QuestionOptionEntity> wrapper = new LambdaQueryWrapper<QuestionOptionEntity>()
                .eq(QuestionOptionEntity::getQuestionId, entity.getId())
                .select(QuestionOptionEntity::getTitle);
        List<String> list = optionService.listObjs(wrapper, Object::toString);
        QuestionVO vo = new QuestionVO();
        BeanUtil.copyProperties(entity, vo);
        vo.setOptions(list);
        return vo;
    }

    @Override
    public Page<QuestionResultVO> pageListByMapper(QuestionQueryVO vo, Page page) {
        Page<QuestionResultVO> list = vo.getDuplicateQuery() != YNEnum.YES ? baseMapper.pageList(vo, page) : baseMapper.duplicateListQuery(vo, page);
        list.getRecords().forEach(this::computeFailed);
        return list;
    }

    private void computeFailed(QuestionResultVO vo) {
        if (Objects.nonNull(vo.getErrorCount()) && Objects.nonNull(vo.getTotalCount())) {
            double result = (double) vo.getErrorCount() / (double) vo.getTotalCount() * 100;
            vo.setFailed(String.format("%.2f%%", result));
        }
    }

    @Override
    public Boolean checkDuplicate(String title) {
        return Objects.nonNull(baseMapper.containsTitle(title));
    }

    @Override
    public List<QuestionCountByCategory> questionCountByCategory() {
        return baseMapper.questionCountByCategory();
    }

}
