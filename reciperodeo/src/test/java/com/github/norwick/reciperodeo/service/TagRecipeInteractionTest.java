package com.github.norwick.reciperodeo.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.Tag;

@SpringBootTest
class TagRecipeInteractionTest {
	@Autowired
	TagService ts;
	
	@Autowired
	RecipeService rs;
	
	@Test
	void testEverything() {
		String t1N = "yoo";
		Tag t1 = ts.saveTag(new Tag(t1N));
		
		String t2N = "boo";
		Tag t2 = ts.saveTag(new Tag(t2N));
		String t3N = "waggle";
		Tag t3 = ts.saveTag(new Tag(t3N));
		String r1T = "How 2 save a lief";
		Recipe r1 = rs.saveRecipe(new Recipe(r1T));
		String r2T = "Electric Bugalee";
		Recipe r2 = rs.saveRecipe(new Recipe(r2T));

		Assertions.assertTrue(ts.findByName(t1N).isPresent());
		Assertions.assertTrue(ts.findByName(t2N).isPresent());
		Assertions.assertTrue(ts.findByName(t3N).isPresent());

		Assertions.assertTrue(rs.findById(r1.getId()).isPresent());
		Assertions.assertTrue(rs.findById(r2.getId()).isPresent());
		
		t1.addRecipe(r1);
		t1 = ts.saveTag(t1);
		Assertions.assertEquals(1, ts.findByNameContaining(t1N).size());
		Assertions.assertEquals(1, rs.findByTitleContaining(r2T).size());
		Assertions.assertTrue(t1.getRecipes().contains(r1));
		Assertions.assertTrue(rs.findById(r1.getId()).get().getTags().contains(t1));

		t2.addRecipe(r2);
		t2 = ts.saveTag(t2);
		Assertions.assertEquals(1, ts.findByNameContaining(t2N).size());
		Assertions.assertEquals(1, rs.findByTitleContaining(r2T).size());
		
		t3.addRecipe(r1);
		t3.addRecipe(r2);
		t3 = ts.saveTag(t3);
		
		Assertions.assertEquals(1, ts.findByNameContaining(t1N).size());
		Assertions.assertEquals(1, ts.findByNameContaining(t2N).size());
		Assertions.assertEquals(1, ts.findByNameContaining(t3N).size());
		Assertions.assertEquals(1, rs.findByTitleContaining(r1T).size());
		Assertions.assertEquals(1, rs.findByTitleContaining(r2T).size());
		
		r1 = rs.findById(r1.getId()).get();
		r2 = rs.findById(r2.getId()).get();
		Assertions.assertTrue(r1.getTags().contains(t3));
		Assertions.assertTrue(r1.getTags().contains(t1));
		Assertions.assertTrue(r2.getTags().contains(t3));
		Assertions.assertTrue(r2.getTags().contains(t2));
		
		rs.removeRecipe(r2);
		t3 = ts.findById(t3.getId()).get();
		Assertions.assertFalse(t3.getRecipes().contains(r2));
		Assertions.assertTrue(t3.getRecipes().contains(r1));
		
		ts.removeTag(t3);
		r1 = rs.findById(r1.getId()).get();
		Assertions.assertFalse(r1.getTags().contains(t3));
		Assertions.assertTrue(r1.getTags().contains(t1));
		
		ts.removeTag(ts.findById(t1.getId()).get());
		ts.removeTag(ts.findById(t2.getId()).get());
		rs.removeRecipe(rs.findById(r1.getId()).get());
	}
}
