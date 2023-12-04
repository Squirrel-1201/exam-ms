package exam.web.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ssm.common.convert.*;
import ssm.common.enums.BaseEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @date: 2021/11/10 10:18
 * @author: qzc
 * @version: 1.0.
 */
@Configuration
public class ObjectMapperConfig {
    @Bean
    @ConditionalOnClass(ObjectMapper.class)
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        simpleModule.addSerializer(BaseEnum.class, new BaseEnumSerializer());
        simpleModule.addDeserializer(Enum.class, new BaseEnumDeserializer());

        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeJsonSerializer());
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeJsonDeserializer());
        simpleModule.addSerializer(LocalDate.class, new LocalDateJsonSerializer());
        simpleModule.addDeserializer(LocalDate.class,new LocalDateJsonDeserializer());

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.registerModule(simpleModule);
        return  objectMapper;
    }
}
