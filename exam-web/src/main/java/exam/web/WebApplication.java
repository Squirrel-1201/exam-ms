package exam.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import ssm.common.annotation.*;

@SpringBootApplication(scanBasePackages = {"exam"})
@EnableSwagger2
@SsmApiVersion
@SsmGlobalException
@SsmMyBatisPlusConfig
@SsmLogAppender
@EnableCaching
@SsmCaptcha
@MapperScan(value = {"exam.dao.mapper"})
@EnableScheduling
//@SsmXssFilter
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
