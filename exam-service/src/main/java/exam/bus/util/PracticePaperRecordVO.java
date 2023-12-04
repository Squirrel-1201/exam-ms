package exam.bus.util;

import exam.dao.entity.PracticeRecordEntity;
import exam.dao.entity.PracticeRecordOptionEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0
 * @date: 2022/7/18 10:10
 * @author: yangbo
 */
@Data
public class PracticePaperRecordVO {

    List<PracticeRecordEntity> recordList = new ArrayList<>();

    List<PracticeRecordOptionEntity> recordOptionList = new ArrayList<>();
}
