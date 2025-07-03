package com.example.gameorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
		scanBasePackages = {
				"com.example.gameorder",
				"com.example.sharedlib"
		}
)
@EnableFeignClients
@EnableDiscoveryClient
public class GameorderApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(GameorderApplication.class, args);
	}

}
