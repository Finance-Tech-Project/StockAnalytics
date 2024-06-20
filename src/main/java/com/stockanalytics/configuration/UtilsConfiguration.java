package com.stockanalytics.configuration;

import com.stockanalytics.util.DataGetter;
import com.stockanalytics.util.DateGetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfiguration {
    @Bean
    DataGetter dataGetter() { return new DataGetter(); }

    @Bean
    DateGetter dateGetter() { return new DateGetter(dataGetter()); }

    @Bean
    String stringGetter() { return ""; }
}
