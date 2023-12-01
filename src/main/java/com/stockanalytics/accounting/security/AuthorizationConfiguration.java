package com.stockanalytics.accounting.security;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
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
						 .mvcMatchers("/**")
						 .permitAll()

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

		return http.build();
	}
}
