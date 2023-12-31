package com.stockanalytics.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

@Configuration
public class ServiceConfiguration {

	 @Bean
	 ModelMapper getModelMapper() {
	        ModelMapper modelMapper = new ModelMapper();
	        modelMapper.getConfiguration()
	                .setFieldMatchingEnabled(true)
	                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
	                .setMatchingStrategy(MatchingStrategies.STRICT);
		 modelMapper.addConverter(new UserAccountToStringConverter());
	        return modelMapper;
	    }

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	    
	    mailSender.setUsername("shkribaev@gmail.com");
	    mailSender.setPassword("ctlwzhalridomkrz");
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}

}
