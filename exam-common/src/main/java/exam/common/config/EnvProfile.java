package exam.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import exam.common.enums.ENVEnum;

@Configuration
@ConfigurationProperties("exam.env")
@Data
public class EnvProfile {

    private ENVEnum profile = ENVEnum.PRO;
}
