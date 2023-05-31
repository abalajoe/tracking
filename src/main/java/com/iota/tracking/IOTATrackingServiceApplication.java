package com.iota.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class IOTATrackingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IOTATrackingServiceApplication.class, args);
	}

	@Bean
	public PasswordEncoder getPasswordEncoder(){
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
