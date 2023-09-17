package com.stockanalytics.accounting.security;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class AuthorizationConfiguration {

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.httpBasic(withDefaults());
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
         http.authorizeRequests(authorize -> authorize
        		.mvcMatchers("/account/register")
       		.permitAll()
       		.mvcMatchers(HttpMethod.GET, "/account/user/{login}","/account/recovery/{login}")
       		.permitAll()
       		.mvcMatchers("/account/user/{login}/role/{role}")
       		.access("#login == authentication.name or hasRole('ADMINISTRATOR')")

       	 .mvcMatchers(HttpMethod.PUT, "/account/user/{login}")
       	 .access("#login == authentication.name or hasRole('ADMINISTRATOR')")
        	    .mvcMatchers(HttpMethod.DELETE, "/account/user/{login}")
       	    .access("#login == authentication.name or hasRole('ADMINISTRATOR')")
   	    
        	    .anyRequest()
        	    .authenticated()
       );
		
		return http.build();
	}
}
