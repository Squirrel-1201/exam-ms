package exam.web.config;

import exam.common.config.EnvProfile;
import exam.common.enums.ENVEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Autowired
    private EnvProfile profile;

    @Bean
    public Docket buildDocket() {
        // 进行token设置
        ParameterBuilder tokenParam = new ParameterBuilder();
        List<Parameter> parameters = new ArrayList<>();
        tokenParam.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        parameters.add(tokenParam.build());

        // 进行生成swagger文档的配置
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(profile.getProfile() == ENVEnum.DEV)
                .apiInfo(appInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("exam.web.controller"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .globalOperationParameters(parameters); // 配置全局token参数

    }

    /**
     * 生成swagger文档说明
     * @return
     */
    private ApiInfo appInfo() {
        return new ApiInfoBuilder()
                .title("考试系统")
                .description("内部考试系统")
                .version("1.0.0")
                .build();
    }
}