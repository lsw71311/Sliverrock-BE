package com.example.silverrock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing // jpa 감사 기능 활성화
@SpringBootApplication
@EntityScan("com.example.silverrock")
@EnableJpaRepositories("com.example.silverrock")
public class SilverrockApplication {

	public static void main(String[] args) {
		SpringApplication.run(SilverrockApplication.class, args);
	}

}
