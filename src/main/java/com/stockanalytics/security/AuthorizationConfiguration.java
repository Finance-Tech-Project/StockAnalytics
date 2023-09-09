package telran.fintech.security;

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
        		.requestMatchers("/account/register", "/forum/posts/**")
        			.permitAll()
        		.requestMatchers("/account/user/{login}/role/{role}")
        			.hasRole("ADMINISTRATOR")
        		.requestMatchers(HttpMethod.PUT, "/account/user/{login}")
        			.access("#login == authentication.name")
        		.requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
        			.access("#login == authentication.name or hasRole('ADMINISTRATOR')")
        		.requestMatchers(HttpMethod.POST, "/forum/post/{author}")
        			.access("#author == authentication.name")
        		.requestMatchers(HttpMethod.PUT, "/forum/post/{id}/comment/{author}")
        			.access("#author == authentication.name")
        		.requestMatchers(HttpMethod.PUT, "/forum/post/{id}")
        			.access("@customSecurity.checkPostAuthor(#id, authentication.name)")
        		.requestMatchers(HttpMethod.DELETE, "/forum/post/{id}")
        			.access("@customSecurity.checkPostAuthor(#id, authentication.name) or hasRole('MODERATOR')")
        		.anyRequest()
        			.authenticated()
        );
		
		return http.build();
	}
}
