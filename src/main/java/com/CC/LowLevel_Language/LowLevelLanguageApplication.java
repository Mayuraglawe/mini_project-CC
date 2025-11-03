package com.CC.LowLevel_Language;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

 @SpringBootApplication
 @ComponentScan(basePackages = "com.CC.LowLevel_Language")
 @EntityScan(basePackages = "com.CC.LowLevel_Language")
public class LowLevelLanguageApplication {

	public static void main(String[] args) {
		SpringApplication.run(LowLevelLanguageApplication.class, args);
	}
}
