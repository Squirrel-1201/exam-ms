package exam.bus.util;

import cn.hutool.core.collection.CollUtil;
import exam.dao.entity.ExamRecordEntity;
import exam.dao.entity.ExamRecordOptionEntity;
import lombok.Data;

import java.util.List;

@Data
public class StuExamRecordQuestionVO {

    List<ExamRecordEntity> recordList = CollUtil.newArrayList();

    List<ExamRecordOptionEntity> optionList = CollUtil.newArrayList();
}
