package com.github.norwick.reciperodeo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Tells Spring to load all configurations and create the application
 */
@SpringBootApplication()
public class RecipeRodeoApplication {
	
	/**
	 * Runs the application
	 * @param args unused
	 */
	public static void main(String[] args) {
		SpringApplication.run(RecipeRodeoApplication.class, args);
	}

	/**
	 * Supplies bcrypt encoder for encoding passwords
	 * @return encoder
	 */
	@Bean
	public BCryptPasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}
}
