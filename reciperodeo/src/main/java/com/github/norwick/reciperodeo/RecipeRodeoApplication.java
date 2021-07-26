package com.github.norwick.reciperodeo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Tells Spring to load all configurations and create the application
 */
@SpringBootApplication
public class RecipeRodeoApplication {

	@SuppressWarnings("javadoc")
	public static void main(String[] args) {
		SpringApplication.run(RecipeRodeoApplication.class, args);
	}

}
