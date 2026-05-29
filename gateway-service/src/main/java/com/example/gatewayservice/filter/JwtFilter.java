package com.example.gatewayservice.filter;

import com.example.gatewayservice.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtFilter implements GlobalFilter {
    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest()
                .getPath()
                .toString();

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange
                .getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            jwtService.validateToken(token);
        } catch (Exception e) {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Claims claims = jwtService.extractClaim(token);

        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .header("X-User-Id", claims.get("userId").toString())
                .header("X-User-Role", claims.get("role").toString())
                .build();

        return chain.filter(
                exchange.mutate()
                        .request(request)
                        .build());
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/users/login")
                || path.startsWith("/users/register")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");
    }
}
