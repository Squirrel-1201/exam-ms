package exam.web.job;

import exam.bus.ILogService;
import exam.bus.IStuExamService;
import exam.bus.IStuPracticeService;
import exam.common.config.ExamCronConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class ExamSchedulingConfigurer implements org.springframework.scheduling.annotation.SchedulingConfigurer {

    private ExamCronConfig examCronConfig;

    private IStuExamService stuExamService;

    private ILogService logService;

    private IStuPracticeService stuPracticeService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //自动交卷
        taskRegistrar.addCronTask(this.stuExamService::autoSubmitExam, this.examCronConfig.getPaper());
        //自动阅卷
        taskRegistrar.addCronTask(this.stuExamService::autoSubmitMarking, this.examCronConfig.getMaking());
        //自动清除日志
        taskRegistrar.addCronTask(this.logService::autoClearLog, this.examCronConfig.getLog());
        //自动结束试卷
        taskRegistrar.addCronTask(this.stuExamService::autoEndExam, this.examCronConfig.getEndExam());
        //自动结束练习
        taskRegistrar.addCronTask(stuPracticeService::autoEndPractice, examCronConfig.getEndPractice());
        //练习自动阅卷
        taskRegistrar.addCronTask(stuPracticeService::autoMarking, examCronConfig.getPracticeMaking());


    }
}
