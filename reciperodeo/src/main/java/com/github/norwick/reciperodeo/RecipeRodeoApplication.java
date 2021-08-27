package com.github.norwick.reciperodeo;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Tells Spring to load all configurations and create the application
 */
@SpringBootApplication
public class RecipeRodeoApplication implements WebMvcConfigurer {
	
	@SuppressWarnings("javadoc")
	public static void main(String[] args) {
		SpringApplication.run(RecipeRodeoApplication.class, args);
	}

	@SuppressWarnings("javadoc")
	@Bean
	public BCryptPasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}
}
