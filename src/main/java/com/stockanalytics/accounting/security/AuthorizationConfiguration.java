package com.stockanalytics.accounting.security;
import com.stockanalytics.configuration.CorsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class AuthorizationConfiguration {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(CorsConfig.corsConfigurationSource()))
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/account/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/account/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/statistics").permitAll()
                        .requestMatchers(HttpMethod.GET, "/analytics/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/service/addSymbols").permitAll()
                        .anyRequest()
                        .authenticated()
                );
        return http.build();
    }
}
