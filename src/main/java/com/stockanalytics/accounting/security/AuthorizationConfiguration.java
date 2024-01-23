package com.stockanalytics.accounting.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class AuthorizationConfiguration {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/**").permitAll()
                        .requestMatchers(
                                "/account/login",
                                "/account/register",
                                "/statistics",
                                "/analytics/**",
                                "/service/addSymbols"
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                );
        return http.build();
    }
}
