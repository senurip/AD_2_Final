package com.example.apigateway.filter;

import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private RouteValidator validator;

    @Autowired
    private RestTemplate template;


    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            logger.info("[API-GATEWAY] AuthenticationFilter invoked for path: {}", path);
            System.out.println("[API-GATEWAY] Request path: " + path + ", isSecured: " + validator.isSecured.test(exchange.getRequest()));
            if (validator.isSecured.test(exchange.getRequest())) {
                logger.info("[API-GATEWAY] Secured endpoint, checking for Authorization header.");
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    logger.warn("[API-GATEWAY] Missing Authorization header.");
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                } else {
                    logger.warn("[API-GATEWAY] Invalid Authorization header format.");
                    throw new RuntimeException("invalid authorization header");
                }

                try {
                    String username = extractUsernameFromToken(authHeader);
                    logger.info("[API-GATEWAY] Extracted username from token: {}", username);
                    logger.info("[API-GATEWAY] Token: {}", authHeader);
                    // REST call to user service with token and username as query params
                    String url = "http://localhost:8080/user-service/api/v1/users/validate-token?token=" + authHeader + "&username=" + username;
                    Boolean isValid = template.getForObject(url, Boolean.class);
                    logger.info("[API-GATEWAY] Token validation result from user-service: {}", isValid);
                    if (isValid == null || !isValid) {
                        logger.warn("[API-GATEWAY] Unauthorized access to application.");
                        throw new RuntimeException("un authorized access to application");
                    }
                } catch (Exception e) {
                    logger.error("[API-GATEWAY] Exception during token validation: {}", e.getMessage());
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            } else {
                logger.info("[API-GATEWAY] Public endpoint, no authentication required.");
            }
            return chain.filter(exchange);
        });
    }

    // Helper method to extract username from JWT token (without signature validation)
    private String extractUsernameFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            int subIndex = payload.indexOf("\"sub\":\"");
            if (subIndex == -1) return null;
            int start = subIndex + 7;
            int end = payload.indexOf("\"", start);
            return payload.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }

    public static class Config {

    }
}
