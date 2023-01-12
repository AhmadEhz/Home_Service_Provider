package org.homeservice.config;

import org.homeservice.repository.PersonRepository;
import org.homeservice.service.AdminService;
import org.homeservice.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final PersonService personService;

    public SecurityConfig(PasswordEncoder passwordEncoder, PersonService personService) {
        this.passwordEncoder = passwordEncoder;
        this.personService = personService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(request -> request.requestMatchers("/admin/save/**").permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers("/admin/**").hasRole("ADMIN"))

                .authorizeHttpRequests(request -> request.requestMatchers(
                        "/specialist/save/**", "/specialist/verifyEmail/**").permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers("/specialist/**")
                        .hasRole("SPECIALIST"))

                .authorizeHttpRequests(request -> request.requestMatchers(
                        "/customer/save/**", "/customer/verifyEmail/**", "/customer/payment/**").permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers("/customer/**").authenticated())
                .authorizeHttpRequests(request -> request.anyRequest().permitAll())
                .httpBasic();
//                .authorizeHttpRequests().anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(personService);
        return provider;
    }
}
