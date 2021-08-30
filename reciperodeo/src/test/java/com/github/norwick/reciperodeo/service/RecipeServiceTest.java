package com.github.norwick.reciperodeo.service;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.Recipe.Visibility;

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
		String t1 = "A";
		String t2 = "B";
		String t3 = "C";

		Recipe r1 = new Recipe(t1);
		r1.setState(Visibility.PUBLIC);
		r1 = rs.saveRecipe(r1);

		Recipe r2 = new Recipe(t2);
		r2.setState(Visibility.PUBLIC);
		r2 = rs.saveRecipe(r2);
		

		Recipe r3 = new Recipe(t3);
		r3.setState(Visibility.PRIVATE);
		r3 = rs.saveRecipe(r3);
		
		List<Recipe> lr = rs.findByStateOrderByCreationTimestamp(Recipe.Visibility.PRIVATE, 0, 10);
		Assertions.assertEquals(1, lr.size());
		lr = rs.findByStateOrderByCreationTimestamp(Recipe.Visibility.PUBLIC, 0, 10);
		Assertions.assertEquals(2, lr.size());
		Assertions.assertEquals(r2, lr.get(0));
		lr = rs.findByStateOrderByCreationTimestamp(Recipe.Visibility.PUBLIC, 1, 1);
		Assertions.assertEquals(1, lr.size());
		Assertions.assertEquals(r1, lr.get(0));
		
		lr = rs.findAll();
		lr.forEach(r -> rs.removeRecipe(r));
	}
	
	//needs empty database to test, so visual confirmation for now
	@Test
	void testSearch() {
		String t1 = "A";
		String t2 = "BC";
		String t3 = "C";
		String t4 = "DC";

		Recipe r1 = new Recipe(t1);
		r1.setState(Visibility.PUBLIC);
		r1 = rs.saveRecipe(r1);

		Recipe r2 = new Recipe(t2);
		r2.setState(Visibility.PUBLIC);
		r2 = rs.saveRecipe(r2);

		Recipe r3 = new Recipe(t3);
		r3.setState(Visibility.PRIVATE);
		r3 = rs.saveRecipe(r3);

		Recipe r4 = new Recipe(t4);
		r4.setState(Visibility.PUBLIC);
		r4 = rs.saveRecipe(r4);
		
		List<Recipe> lr = rs.searchPublicRecipes("A", 0, 10).getContent();
		Assertions.assertEquals(1, lr.size());
		lr = rs.searchPublicRecipes("C", 0, 10).getContent();
		Assertions.assertEquals(2, lr.size());
		Assertions.assertEquals(r4, lr.get(0));
		lr = rs.searchPublicRecipes("C", 1, 1).getContent();
		Assertions.assertEquals(1, lr.size());
		Assertions.assertEquals(r2, lr.get(0));
		
		lr = rs.findAll();
		lr.forEach(r -> rs.removeRecipe(r));
	}

}
