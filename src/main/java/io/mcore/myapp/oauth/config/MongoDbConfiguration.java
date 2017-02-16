package io.mcore.myapp.oauth.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import io.mcore.myapp.oauth.repository.converters.MongoAuthorizationReadConverter;

@Configuration
public class MongoDbConfiguration {

	@Bean
	public CustomConversions customConversions() {
		List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
		MongoAuthorizationReadConverter converter = new MongoAuthorizationReadConverter();
		converterList.add(converter);
		return new CustomConversions(converterList);
	}

}
