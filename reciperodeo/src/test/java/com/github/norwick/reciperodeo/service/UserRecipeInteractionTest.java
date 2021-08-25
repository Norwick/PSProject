package com.github.norwick.reciperodeo.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.RecipeAccess;
import com.github.norwick.reciperodeo.domain.RecipeAccess.Access;
import com.github.norwick.reciperodeo.domain.User;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class UserRecipeInteractionTest {

	@Autowired
	UserService us;
	
	@Autowired
	RecipeService rs;
	
	@Test
	@Order(1)
	void testStart() {
		User[] u = new User[3];
		u[0] = us.saveUser(new User("Betty","betty@crocker.com",true,"yublker"));
		u[1] = us.saveUser(new User("Bob","b@b.b",true,"nyarg"));
		u[2] = us.saveUser(new User("Beth","s@g.c",true,"shi"));

		Recipe[] r = new Recipe[3];
		r[0] = rs.saveRecipe(new Recipe("shum"));
		r[1] = rs.saveRecipe(new Recipe("sho"));
		r[2] = rs.saveRecipe(new Recipe("sum"));
		
		for (int i = 0; i < 3; ++i) {
			Assertions.assertTrue(us.findById(u[i].getId()).isPresent());
			Assertions.assertTrue(rs.findById(r[i].getId()).isPresent());
		}
	}
	
	@Test
	void testEverything() {
		try {
			List<Recipe> recipes = rs.findAll();
			List<User> users = us.findAll();
			//basic 'add and change relationship'
			RecipeAccess ra = rs.setUser(recipes.get(0),users.get(0), Access.OWNER);
			users.set(0, ra.getUser());
			recipes.set(0, ra.getRecipe());
			
			ra = rs.setUser(recipes.get(0), users.get(1), Access.VIEWER);
			users.set(1, ra.getUser());
			recipes.set(0, ra.getRecipe());
			
			ra = rs.setUser(recipes.get(0), users.get(1), Access.EDITOR);
			users.set(1, ra.getUser());
			recipes.set(0, ra.getRecipe());
			
			Assertions.assertEquals(2, recipes.get(0).getRecipeAccesses().size());
			Assertions.assertEquals(1, users.get(0).getRecipeAccesses().size());
			Assertions.assertEquals(1, users.get(1).getRecipeAccesses().size());
			Assertions.assertEquals(0, users.get(2).getRecipeAccesses().size());
			Assertions.assertEquals(0, recipes.get(1).getRecipeAccesses().size());
			Assertions.assertEquals(0, recipes.get(2).getRecipeAccesses().size());
	
			ra = rs.setUser(recipes.get(2), users.get(1), Access.OWNER);
			users.set(1, ra.getUser());
			recipes.set(0, ra.getRecipe());
			
			ra = rs.setUser(recipes.get(2), users.get(2), Access.VIEWER);
			
			rs.removeUser(recipes.get(0), users.get(0));
			Optional<Recipe> or = rs.findById(recipes.get(0).getId());
			Assertions.assertTrue(or.isPresent());
			recipes.set(0, or.get());
			Assertions.assertEquals(1, recipes.get(0).getRecipeAccesses().size());
			Optional<User> ou = us.findById(users.get(0).getId());
			Assertions.assertTrue(ou.isPresent());
			users.set(0, ou.get());
			Assertions.assertTrue(users.get(0).getRecipeAccesses().isEmpty());
			ou = us.findById(users.get(1).getId());
			Assertions.assertTrue(ou.isPresent());
			users.set(1, ou.get());
			Assertions.assertEquals(2, users.get(1).getRecipeAccesses().size());
			Set<RecipeAccess> ras = recipes.get(0).getRecipeAccesses();
			for (RecipeAccess cur : ras) {
				Assertions.assertEquals(users.get(1), cur.getUser());
			}
			
			
		} catch(IllegalRelationshipException e) {
			Assertions.fail();
		}
	}

	@Test
	@Order(10)
	void testEnd() {
		List<User> u = us.findAll();
		for (User user : u) {
			us.removeUser(user);
		}
		
		List<Recipe> r = rs.findAll();
		for (Recipe recipe : r) {
			rs.removeRecipe(recipe);
		}
		Assertions.assertEquals(0, us.findAll().size());
		Assertions.assertEquals(0, rs.findAll().size());
	}
	
}
