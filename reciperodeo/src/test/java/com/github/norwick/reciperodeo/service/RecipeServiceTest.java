package com.github.norwick.reciperodeo.service;

import java.util.List;
import java.util.UUID;

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

}
