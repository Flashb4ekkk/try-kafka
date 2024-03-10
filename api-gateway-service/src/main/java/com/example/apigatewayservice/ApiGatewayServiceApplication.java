package com.example.apigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayServiceApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/api/user/**")
						.filters(f -> f.filter((exchange, chain) -> {
							ServerHttpRequest request = exchange.getRequest().mutate()
									.header("X-CSRF-TOKEN", "<token>")
									.build();
							return chain.filter(exchange.mutate().request(request).build());
						}))
						.uri("http://localhost:8082"))
				.route(p -> p
						.path("/login/**")
						.filters(f -> f.filter((exchange, chain) -> {
							ServerHttpRequest request = exchange.getRequest().mutate()
									.header("X-CSRF-TOKEN", "<token>")
									.build();
							return chain.filter(exchange.mutate().request(request).build());
						}))
						.uri("http://localhost:8082"))
				.route(p -> p
						.path("/api/book/**")
						.filters(f -> f.filter((exchange, chain) -> {
							ServerHttpRequest request = exchange.getRequest().mutate()
									.header("X-CSRF-TOKEN", "<token>")
									.build();
							return chain.filter(exchange.mutate().request(request).build());
						}))
						.uri("http://localhost:8083"))
//				.route(p -> p
//						.path("/api/wishlist/**")
//						.uri("http://localhost:8084"))
//				.route(p -> p
//						.path("/api/purchase/**")
//						.uri("http://localhost:8085"))
//				.route(p -> p
//						.path("/api/review/**")
//						.uri("http://localhost:8086"))
				.build();
	}
}
