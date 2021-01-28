package com.iso.logus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LogusApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogusApplication.class, args);
	}

}
