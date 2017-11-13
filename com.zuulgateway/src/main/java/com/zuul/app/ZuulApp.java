package com.zuul.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.zuul.filters.ErrorFilter;
import com.zuul.filters.PostFilter;
import com.zuul.filters.PreFilter;
import com.zuul.filters.RouteFilter;


@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class ZuulApp {

	public static void main(String[] args) {
		

		System.setProperty("spring.application.name", "Zuul Service");
		System.setProperty("eureka.client.serviceUrl.defaultZone", "http://localhost:8090/eureka");
		System.setProperty("server.port", "8079");

		
		SpringApplication.run(ZuulApp.class, args);
	}

	@Bean
	public PreFilter preFilter() {
		return new PreFilter();
	}

	@Bean
	public PostFilter postFilter() {
		return new PostFilter();
	}

	@Bean
	public ErrorFilter errorFilter() {
		return new ErrorFilter();
	}

	@Bean
	public RouteFilter routeFilter() {
		return new RouteFilter();
	}
}
