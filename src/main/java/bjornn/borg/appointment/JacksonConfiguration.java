package bjornn.borg.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import bjornn.borg.appointment.controller.serializer.CustomModule;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonConfiguration {
	
	@Autowired
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper mapper = builder.createXmlMapper(false).build();
		mapper.registerModule(new CustomModule());
		return mapper;
	}
	
//	@Override
//	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//	    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//	    builder.dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm")).indentOutput(true);
//	    builder.timeZone("GMT-3");
//	    builder.mo
//	}
}
