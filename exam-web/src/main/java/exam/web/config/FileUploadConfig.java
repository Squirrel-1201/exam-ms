package exam.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.MultipartConfigElement;

/**
 * @Auther: zhoufs
 * @Date: 2022/6/11 17:29
 * @Description:  上传文件的大小配置
 */
@Configuration
public class FileUploadConfig {

    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver getStandardServletMultipartResolver(){
        return new StandardServletMultipartResolver();
    }

    @Value("${spring.http.server.upload.maxFileSize:10}")
    private Integer maxFileSize;
    @Value("${spring.http.server.upload.maxRequestSize:20}")
    private Integer maxRequestSize;

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofBytes(1024*1024*maxFileSize));
        factory.setMaxRequestSize(DataSize.ofBytes(1024*1024*maxRequestSize));
        return factory.createMultipartConfig();
    }
}
