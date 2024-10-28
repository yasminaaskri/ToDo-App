package com.springboot.ToDoApp.security;


import static org.springframework.security.config.Customizer.withDefaults;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {

	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	 
	private UserDetails createNewUser(String username ,String password) {
		
		Function<String , String >   passwordEncoder =
				input ->passwordEncoder().encode(input);
				
		UserDetails userdetails = User.builder()
				                                 .passwordEncoder(passwordEncoder)
				                                 .username(username)			                                 
				                                 .password(password)
				                                 .roles("USER","ADMIN")
				                                 .build();
		return userdetails ;
				                             
	}
	
	@Bean 
	public InMemoryUserDetailsManager createUserDetailsManager() {
		
		UserDetails userDetrails1 =  createNewUser ("yasmine" ,"123456");
		UserDetails userDetrails2 =  createNewUser ("asma" ,"hello123");
		
		return new InMemoryUserDetailsManager(userDetrails1, userDetrails2);
		
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(
				auth -> auth.anyRequest().authenticated());
		http.formLogin(withDefaults());

		http.csrf(csrf -> csrf.disable());
		http.headers(headers -> headers.frameOptions(frameOptionsConfig-> frameOptionsConfig.disable()));
		
		return http.build();
	}


	
}
