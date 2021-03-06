package com.vuebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@ComponentScan(basePackages = "com.vuebackend.controllers,com.vuebackend.communication,com.vuebackend.jwt,com.vuebackend.security")
@EntityScan("com.vuebackend.entities")
@SpringBootApplication
public class VueBackendAuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(VueBackendAuthApplication.class, args);
	}
}
