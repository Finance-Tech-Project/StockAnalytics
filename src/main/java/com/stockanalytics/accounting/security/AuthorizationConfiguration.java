package com.stockanalytics.accounting.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration

public class AuthorizationConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private CorsConfigurationSource corsConfigurationSource;

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Замените на свой фронтенд URL
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}


	@Bean
	protected void configure(HttpSecurity http) throws Exception {
		http
				.cors().configurationSource(corsConfigurationSource) // Интеграция CORS с Spring Security
				.and()
				.httpBasic(withDefaults())
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.authorizeRequests(authorize -> authorize
								.mvcMatchers("/**").permitAll()

//
//        		.mvcMatchers("/start/remove","/start/add","/service/addsymbols","/account/register","/start/symbols", "/statistics?ticker={tickerName}", "/searchSymbol/?search={searchedTickerForLetters}", "/quote/history?dateFrom={dateFrom}&dateTo={dateTo}&ticker={tickerName}")
//       		.permitAll()
//       		.mvcMatchers(HttpMethod.GET,"/allsymbols","/account/user/{login}","/account/recovery/{login}","/quote/history","/statistics")
//       		.permitAll()
//       		.mvcMatchers("/account/user/{login}/role/{role}")
//       		.access("#login == authentication.name or hasRole('ADMINISTRATOR')")
//
//       	 .mvcMatchers(HttpMethod.PUT, "/account/user/{login}")
//       	 .access("#login == authentication.name or hasRole('ADMINISTRATOR')")
//        	    .mvcMatchers(HttpMethod.DELETE, "/account/user/{login}")
//       	    .access("#login == authentication.name or hasRole('ADMINISTRATOR')")
//
 	    .anyRequest()
  	    .authenticated()
       );


	}
}
