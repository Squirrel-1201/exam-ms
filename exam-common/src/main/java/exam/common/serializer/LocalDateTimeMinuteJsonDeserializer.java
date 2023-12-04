package exam.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.SneakyThrows;
import ssm.common.constant.DateCommonConstant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeMinuteJsonDeserializer extends JsonDeserializer<LocalDateTime>{
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateCommonConstant.TIME_PATTERN_YYYY_MM_DD_HH_MM);
	@SneakyThrows
	@Override
	public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)  {
		String value = jsonParser.getText();
		return value == null ? null : LocalDateTime.parse(value, dateTimeFormatter);
	}
}
