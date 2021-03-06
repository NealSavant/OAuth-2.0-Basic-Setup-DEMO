package com.example.demo;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private CustomOAuth2UserService userService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
		// all requests to controller require authentication
		.authorizeRequests(a -> a
				.antMatchers("/user").permitAll()
				.anyRequest().authenticated()
				)
		// cors policy
		.cors().configurationSource(corsConfigurationSource())
		
		.and()
		// login configuration
		
		.oauth2Login()
			.userInfoEndpoint()
				.userService(userService)
			.and()
			.defaultSuccessUrl("http://localhost:4200/home")
			.failureUrl("http://localhost:4200");		
	}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
    	CorsConfiguration configuration = new CorsConfiguration();
    	configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
    	configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT"));
    	configuration.setAllowedHeaders(Arrays.asList("Content-Type", "credentials"));
    	configuration.setAllowCredentials(true);
    	
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", configuration);
    	
    	return source;
    }
}
