package com.stockanalytics.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.stockanalytics.util.DateGetter;

@Configuration
public class DateGetterConfig {
    @Bean
    DateGetter dateGetter() {
        return new DateGetter();
    }
}
