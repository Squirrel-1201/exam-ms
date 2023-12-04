package exam.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("exam.user.login")
@Data
public class UserLoginConfig {

    private int failCount = 5;

    private int lockMinutes = 60 * 24;

    /**
     * 重置密码(口令) 默认 123456   前端加密后的字符串 207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb
     */
    private String initPwd = "207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb";
}
