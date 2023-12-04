package exam.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("exam.cron")
public class ExamCronConfig {

    /**
     * 自动交卷cron表达式
     */
    private String paper = "30 * * * * ?";

    /**
     * 自动阅卷cron表达式
     */
    private String making = "0 * * * * ?";

    /**
     * 自动清除日志cron表达式
     */
    private String log = "0 0 2 * * ?";

    /**
     * 自动结束试卷cron表达式
     */
    private String endExam = "0 0/5 * * * ?";

    /**
     * 练习试卷自动阅卷
     */
    private String practiceMaking = "30 * * * * ?";

    /**
     * 练习自动结束
     */
    private String endPractice = "0 * * * * ?";
}
