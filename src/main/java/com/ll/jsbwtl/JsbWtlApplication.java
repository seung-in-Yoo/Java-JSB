package com.ll.jsbwtl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties
public class JsbWtlApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsbWtlApplication.class, args);
	}
}
