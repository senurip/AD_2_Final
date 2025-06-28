package com.example.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final Logger logger = LoggerFactory.getLogger(RouteValidator.class);

    public static final List<String> openApiEndpoints = List.of(
            "/user-service/api/v1/users/register",
            "/user-service/api/v1/users/login",
            "/user-service/api/v1/users/validate-token",
            "/payment-service/payment-service/api/v1/payments/checkPayment"
    );

    // Secure all except open endpoints (flexible for trailing slashes, etc.)
    public Predicate<ServerHttpRequest> isSecured =
            request -> {
                String path = request.getURI().getPath();
                boolean secured = openApiEndpoints
                        .stream()
                        .noneMatch(uri -> path.startsWith(uri));
                logger.info("[API-GATEWAY] RouteValidator: path={}, secured={}", path, secured);
                System.out.println("[API-GATEWAY] RouteValidator: path=" + path + ", secured=" + secured);
                return secured;
            };
}
