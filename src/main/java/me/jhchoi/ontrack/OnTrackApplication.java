package me.jhchoi.ontrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class OnTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnTrackApplication.class, args);
	}

}
