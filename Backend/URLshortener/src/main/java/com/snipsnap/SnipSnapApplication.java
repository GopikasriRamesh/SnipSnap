package com.snipsnap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.snipsnap")
public class SnipSnapApplication {
	public static void main(String[] args) {
		SpringApplication.run(SnipSnapApplication.class, args);
	}
}
