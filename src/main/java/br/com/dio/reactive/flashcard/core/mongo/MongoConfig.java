package br.com.dio.reactive.flashcard.core.mongo;

import br.com.dio.reactive.flashcard.core.mongo.converter.DateToOffsetDateTimeConverter;
import br.com.dio.reactive.flashcard.core.mongo.converter.OffsetDateTimeToDateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class MongoConfig {

    @Bean
    MongoCustomConversions mongoCustomConversions(){
       return new MongoCustomConversions(List.of(new OffsetDateTimeToDateConverter(), new DateToOffsetDateTimeConverter()));
    }
}
