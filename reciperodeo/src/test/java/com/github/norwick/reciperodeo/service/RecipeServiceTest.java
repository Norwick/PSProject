package com.github.norwick.reciperodeo.service;

import java.util.List;
import java.util.UUID;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.norwick.reciperodeo.domain.Recipe;

/**
 * Tests RecipeService
 * @author Norwick Lee
 */
@SpringBootTest
class RecipeServiceTest {
	
	@Autowired
	private RecipeService rs;
	
	//bad code
	@Test
	void testEverything() {
		long count = rs.count();
		Recipe r = new Recipe("Char Siu Bao");
		rs.saveRecipe(r);
		Assertions.assertEquals(count + 1, rs.count());
		List<Recipe> rl = rs.findByTitleContaining("Siu");
		UUID id = rl.get(0).getId();
		Assertions.assertTrue(rs.existsById(id));
		rs.removeRecipe(rl.get(0));
		Assertions.assertFalse(rs.existsById(id));
		Assertions.assertEquals(count, rs.count());
	}
	
	//needs empty database to test, so visual confirmation for now that number of private is same
	@Test
	void testFindByStateOrderByCreationTimeStamp() {
		System.out.println("Private:\n" + rs.findByStateOrderByCreationTimestamp(Recipe.Visibility.PRIVATE, 0, 10));
		System.out.println("Public:\n" + rs.findByStateOrderByCreationTimestamp(Recipe.Visibility.PUBLIC, 1, 10));
	}
	
	//needs empty database to test, so visual confirmation for now
	@Test
	void testSearch() {
		System.out.println(rs.searchPublicRecipes("b", 0, 10).getContent());
		System.out.println(rs.searchPublicRecipes("B", 0, 10).getContent());
	}

}
