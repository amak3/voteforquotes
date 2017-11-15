package com.voteforquotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class AuthenticationConfiguration extends WebSecurityConfigurerAdapter  {
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(new UserService());
		auth.authenticationProvider(provider);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		System.err.println("get password encoder");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/addQuote").hasRole("USER")
		.and().authorizeRequests().antMatchers("/form").hasRole("USER")
		.and().authorizeRequests().antMatchers("/result").hasRole("USER")
		.and().authorizeRequests().anyRequest().permitAll();
//		.and().authorizeRequests().antMatchers("/start").permitAll()
//		.and().authorizeRequests().antMatchers("/register").permitAll()
//		.and().authorizeRequests().antMatchers("/login").anonymous()
		
		http.formLogin().loginPage("/login").permitAll();
		
		http.csrf().disable();
	}
	

}
