package com.iso.logus.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.iso.logus.global.jwt.JwtAuthenticationFilter;
import com.iso.logus.global.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private final JwtTokenProvider jwtTokenProvider;
	
	private final String[] WHITELIST = {
			"/swagger-resources/**",
			"/swagger-ui.html",
			"/v2/api-docs",
			"/webjars/**",
			"/docs/api-docs.html",
			"/h2-console/**"
	};
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity			
			.httpBasic().disable()
			.csrf().disable()
			//.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.headers().frameOptions().disable()
			.and()
				.authorizeRequests()
					.antMatchers("/api/user/join", "/api/user/login").permitAll()
					.antMatchers(WHITELIST).permitAll()
					.anyRequest().authenticated()
			.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
	}
}
