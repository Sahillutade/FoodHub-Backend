package com.example.foodhub_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class FoodhubBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodhubBackendApplication.class, args);
	}

}
