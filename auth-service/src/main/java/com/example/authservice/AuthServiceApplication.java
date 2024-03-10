package com.example.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/api/user/**")
						.uri("http://localhost:8082"))
				.route(p -> p
						.path("/api/book/**")
						.uri("http://localhost:8083"))
				.route(p -> p
						.path("/api/wishlist/**")
						.uri("http://localhost:8084"))
				.route(p -> p
						.path("/api/purchase/**")
						.uri("http://localhost:8085"))
				.route(p -> p
						.path("/api/review/**")
						.uri("http://localhost:8086"))
				.build();
	}
}
