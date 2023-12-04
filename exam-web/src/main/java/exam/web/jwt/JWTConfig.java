package exam.web.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "exam.jwt")
@Data
public class JWTConfig {

    private String secret = "!QAZXSW@EDCGHJKL><?0fdd2s3";

    /**
     * token有效期（秒）
     */
    private int expire = 1800;
}
