package com.example.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(csrf -> csrf.disable())
            .authorizeExchange(exchange -> exchange
                .anyExchange().permitAll() // Allow all, let AuthenticationFilter handle security
            );
        return http.build();
    }
}
