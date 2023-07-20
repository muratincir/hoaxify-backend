package com.hoaxify.ws.Configuration;



import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import jakarta.servlet.Filter;


@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration extends WebSecurityConfiguration{
	
	@SuppressWarnings({ "removal", "deprecation" })
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.exceptionHandling().authenticationEntryPoint(new AuthEntryPoint());
		
		http.headers().frameOptions().disable();
		
		http
			.authorizeRequests()
				.requestMatchers(HttpMethod.PUT, "/api/1.0/users/{username}").authenticated()
				.requestMatchers(HttpMethod.POST, "/api/1.0/hoaxes").authenticated()
				.requestMatchers(HttpMethod.POST, "/api/1.0/hoax-attachments").authenticated()
				.requestMatchers(HttpMethod.POST, "/api/1.0/logout").authenticated()
			.and()
			.authorizeRequests().anyRequest().permitAll();
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore( tokenFilter(),UsernamePasswordAuthenticationFilter.class);
	}
	
	private Filter tokenFilter() {
		return new com.hoaxify.ws.Configuration.TokenFilter();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}