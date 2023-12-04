package exam.bus.util;

import exam.dao.entity.ExamRecordOptionEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ExamOptionVO {

    private String answer;

    private List<ExamRecordOptionEntity> list;
}
