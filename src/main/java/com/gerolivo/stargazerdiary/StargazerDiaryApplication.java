package com.gerolivo.stargazerdiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StargazerDiaryApplication {
 
	public static void main(String[] args) {
		SpringApplication.run(StargazerDiaryApplication.class, args);
	}
}
