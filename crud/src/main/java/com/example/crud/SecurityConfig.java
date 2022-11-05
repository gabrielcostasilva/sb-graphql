package com.example.crud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager users() {
        UserDetails user = User
                            .withDefaultPasswordEncoder()
                            .username("user")
                            .password("test123")
                            .roles("USER")
                            .build();

        UserDetails admin = User
                            .withDefaultPasswordEncoder()
                            .username("admin")
                            .password("test123")
                            .roles("USER", "ADMIN")
                            .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                    .csrf().disable()
                    .sessionManagement().disable()
                    .authorizeRequests()
                        .anyRequest().authenticated()
                    .and()
                        .httpBasic()
                    .and()
                        .build();
    }
}
