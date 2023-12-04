package exam.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ssm.common.constant.DateCommonConstant;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeMinuteJsonSerializer extends JsonSerializer<LocalDateTime> {

	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		String text = (value == null ? null : value.format(DateCommonConstant.DATETIMEFORMATTER_YYYY_MM_DD_HH_MM));
		if (text != null) {
			gen.writeString(text);
		}
	}

}
