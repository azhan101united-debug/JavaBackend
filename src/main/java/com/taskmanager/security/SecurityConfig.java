package com.taskmanager.security;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
 private final JwtFilter jwtFilter;
 @Bean
 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 http
 .csrf().disable()
 .authorizeHttpRequests(auth -> auth
 .requestMatchers("/api/auth/**").permitAll()
 .anyRequest().authenticated()
 )
 .addFilterBefore(jwtFilter,
 org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
 );
 return http.build();
 }
}