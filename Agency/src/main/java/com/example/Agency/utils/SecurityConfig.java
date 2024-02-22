package com.example.Agency.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("flights/edit/{id}","/hotels/delete/{id}","/flights/delete/{id}","/hotels/new","/flights/new").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin().permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
        return httpSecurity.build();
    }
}
