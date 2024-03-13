package com.example.apigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableWebFluxSecurity
public class ApiGatewayServiceApplication {

	@RequestMapping("/circuitbreakerfallback")
	public String circuitbreakerfallback() {
		return "This is a fallback";
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		//@formatter:off
		return builder.routes()
				.route("user-service", r -> r.path("/api/user/**")
						.uri("http://localhost:8082"))
				.route("book-service", r -> r.path("/api/book/**")
						.uri("http://localhost:8083"))
				.route("wishlist-service", r -> r.path("/api/wishlist/**")
						.uri("http://localhost:8084"))
				.route("purchase-service", r -> r.path("/api/purchase/**")
						.uri("http://localhost:8085"))
				.route("review-service", r -> r.path("/api/review/**")
						.uri("http://localhost:8086"))
				.route("auth-service", r -> r.path("/login/**")
						.uri("http://localhost:8087"))
				.build();
		//@formatter:on
	}

	@Bean
	RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(1, 2);
	}

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http
				.authorizeExchange(exchanges ->
						exchanges
								.pathMatchers("/api/**").authenticated()
								.anyExchange().permitAll()
				)
				.httpBasic(Customizer.withDefaults())
				.csrf(Customizer.withDefaults())
				.build();
	}

	@Bean
	public MapReactiveUserDetailsService reactiveUserDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build();
		return new MapReactiveUserDetailsService(user);
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayServiceApplication.class, args);
	}
}