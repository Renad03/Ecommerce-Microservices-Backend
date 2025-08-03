package com.example.microservices.wallet_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class WalletMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletMicroserviceApplication.class, args);
	}

}
