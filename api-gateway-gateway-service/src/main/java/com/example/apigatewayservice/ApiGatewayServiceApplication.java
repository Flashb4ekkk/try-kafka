package com.example.apigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayServiceApplication {

//	@Bean
//	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route("user-service", r -> r.path("/api/user/**")
//						.uri("http://localhost:8082"))
//				.route("book-service", r -> r.path("/api/book/**")
//						.uri("http://localhost:8083"))
//				.route("wishlist-service", r -> r.path("/api/wishlist/**")
//						.uri("http://localhost:8084"))
//				.route("purchase-service", r -> r.path("/api/purchase/**")
//						.uri("http://localhost:8085"))
//				.route("review-service", r -> r.path("/api/review/**")
//						.uri("http://localhost:8086"))
//				.route("auth-service", r -> r.path("/login/**")
//						.uri("http://localhost:8087"))
//				.build();
//	}

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayServiceApplication.class, args);
	}

}
