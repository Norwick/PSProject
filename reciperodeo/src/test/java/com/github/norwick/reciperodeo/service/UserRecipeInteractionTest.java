package com.github.norwick.reciperodeo.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.User;

//note assumes empty database

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class UserRecipeInteractionTest {

	@Autowired
	UserService us;
	
	@Autowired
	RecipeService rs;
	
	@ParameterizedTest
	@CsvFileSource(resources = "saveUserTest.csv", numLinesToSkip=1)
	@Order(1)
	void addUserTest(String email, String username, boolean searchable, String hash) {
		User u = new User(username,email,searchable,hash);
		u = us.saveUser(u);
		Assertions.assertTrue(us.findByUsername(username).isPresent());
	}
	
	@ParameterizedTest
	@ValueSource(strings={"Raisin Challah","Roasted Broccolini","Butter Kofta","Melon Icecream"})
	@Order(1)
	void addRecipeTest(String title) {
		Recipe r = new Recipe(title);
		rs.saveRecipe(r);
		Assertions.assertEquals(1,rs.findByTitleContaining(title).size());
	}
	
	@Test
	@Order(2)
	void testEverything() {
		List<Recipe> rl = rs.findAll();
		Assertions.assertEquals(4, rl.size());
		List<User> ul = us.findAll();
		Assertions.assertEquals(4, ul.size());
		
		rl.get(0).setUser(ul.get(0));				//u0: r0
		rs.saveRecipe(rl.get(0));
		Optional<User> ou = us.findById(ul.get(0).getId());
		Assertions.assertTrue(ou.isPresent());
		ul.set(0, ou.get());
		Assertions.assertEquals(1, ul.get(0).getRecipes().size());
		Assertions.assertTrue(ul.get(0).getRecipes().contains(rl.get(0)));
		
		rl.get(1).setUser(ul.get(0));
		rl.set(1, rs.saveRecipe(rl.get(1)));
		ou = us.findById(ul.get(0).getId());
		Assertions.assertTrue(ou.isPresent());
		ul.set(0, ou.get());
		Assertions.assertEquals(2, ul.get(0).getRecipes().size());
		Assertions.assertTrue(ul.get(0).getRecipes().contains(rl.get(0)));
		Assertions.assertTrue(ul.get(0).getRecipes().contains(rl.get(1)));
		
		rs.removeRecipe(rl.get(0));
		Assertions.assertTrue(rs.findById(rl.get(0).getId()).isEmpty());
		ou = us.findById(ul.get(0).getId());
		Assertions.assertTrue(ou.isPresent());
		ul.set(0, ou.get());
		Assertions.assertEquals(1, ul.get(0).getRecipes().size());
		Assertions.assertFalse(ul.get(0).getRecipes().contains(rl.get(0)));
		Assertions.assertTrue(ul.get(0).getRecipes().contains(rl.get(1)));
	}
	
	@Test
	@Order(10)
	void removeUserRecipeTest() {
		List<User> users = us.findAll();
		users.forEach(u -> us.removeUser(u));
		List<Recipe> recipes = rs.findAll();
		recipes.forEach(r -> rs.removeRecipe(r));
		Assertions.assertTrue(us.findAll().isEmpty());
		Assertions.assertTrue(rs.findAll().isEmpty());
	}
	
}
