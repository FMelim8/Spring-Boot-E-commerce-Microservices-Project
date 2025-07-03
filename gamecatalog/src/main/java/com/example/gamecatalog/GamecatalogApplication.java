package com.example.gamecatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(
		scanBasePackages = {
				"com.example.gamecatalog",
				"com.example.sharedlib"
		}
)
@EnableDiscoveryClient
public class GamecatalogApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(GamecatalogApplication.class, args);
	}

}
