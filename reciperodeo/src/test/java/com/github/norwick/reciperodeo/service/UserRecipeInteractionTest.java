package com.github.norwick.reciperodeo.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
	void addUserTest(String email, String username, boolean isSearchable, String hash) {
		User u = new User(username,email,isSearchable,hash);
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
