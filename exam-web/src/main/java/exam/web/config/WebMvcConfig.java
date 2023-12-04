package exam.web.config;

import exam.web.interceptor.PermInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ssm.common.convert.EnumConverterFactory;

@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private static String[] excludePathPatterns = new String[]{"/dev/**", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**"};

    private PermInterceptor permInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new EnumConverterFactory());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permInterceptor).excludePathPatterns(excludePathPatterns).order(-2000);
    }
}
