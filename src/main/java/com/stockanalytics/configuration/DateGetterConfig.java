package com.stockanalytics.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.stockanalytics.util.DataGetter;

@Configuration
public class DateGetterConfig {
    @Bean
    DataGetter dateGetter() {
        return new DataGetter();
    }
}
