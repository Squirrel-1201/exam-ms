package exam.bus.util;

import exam.dao.entity.PracticeRecordOptionEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @version 1.0.0
 * @date: 2022/7/18 11:09
 * @author: yangbo
 */
@Data
@Accessors(chain = true)
public class PracticeOptionVO {
    private String answer;

    private List<PracticeRecordOptionEntity> list;
}
