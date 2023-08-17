package com.algaworks.example.serviceregistry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(c-> c.disable())
                .authorizeHttpRequests(c-> {
                    c.requestMatchers("/**").authenticated();
                    c.requestMatchers("/actuator/health").permitAll();
                })
                .httpBasic(withDefaults());
        return http.build();
    }

}


