package org.homeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
//                .authorizeHttpRequests(request -> request.requestMatchers("/admin/**").authenticated())
//
//                .authorizeHttpRequests(request -> request.requestMatchers(
//                        "/specialist/save/**","/specialist/verifyEmail/**").permitAll())
//                .authorizeHttpRequests(request -> request.requestMatchers("/specialist/**").authenticated())
//
//                .authorizeHttpRequests(request -> request.requestMatchers(
//                        "/customer/save/**","/customer/verifyEmail/**").permitAll())
//                .authorizeHttpRequests(request -> request.requestMatchers("/customer/**").authenticated())
//                .httpBasic();
                .authorizeHttpRequests().anyRequest().permitAll();
        return http.build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
