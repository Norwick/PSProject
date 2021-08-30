package com.github.norwick.reciperodeo.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.Recipe.Visibility;
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
	

	
	//needs empty database to test, so visual confirmation for now
	@Test
	void testTagSearch() {
		String t1N = "A";
		String t2N = "B";
		String t3N = "C";
		Tag t1 = ts.saveTag(new Tag(t1N));
		Tag t2 = ts.saveTag(new Tag(t2N));
		Tag t3 = ts.saveTag(new Tag(t3N));
		
		String r1T = "X";
		String r2T = "Y";
		String r3T = "Z";
		Recipe r1 = new Recipe(r1T);
		r1.setState(Visibility.PUBLIC);
		r1 = rs.saveRecipe(r1);
		Recipe r2 = new Recipe(r2T);
		r2.setState(Visibility.PUBLIC);
		r2 = rs.saveRecipe(r2);
		Recipe r3 = new Recipe(r3T);
		r3.setState(Visibility.PRIVATE);
		r3 = rs.saveRecipe(r3);
		
		t3.addRecipe(r1);
		t3.addRecipe(r2);
		t3.addRecipe(r3);
		t3 = ts.saveTag(t3);
		
		t2.addRecipe(r2);
		t2 = ts.saveTag(t2);
		
		t1.addRecipe(r1);
		t1 = ts.saveTag(t1);
		
		List<Tag> t = new ArrayList<>();
		t.add(ts.findByName(t1N).get());
		t.add(ts.findByName(t3N).get());
		List<Recipe> lr = rs.searchByTag(t, 0, 28).getContent();
		Assertions.assertEquals(1, lr.size());
		Assertions.assertEquals(rs.findByTitleContaining(r1T).get(0), lr.get(0));
		t.remove(0);
		lr = rs.searchByTag(t, 0, 28).getContent();
		Assertions.assertEquals(2, lr.size());
		Assertions.assertTrue(lr.contains(rs.findByTitleContaining(r2T).get(0)));
		Assertions.assertTrue(lr.contains(rs.findByTitleContaining(r1T).get(0)));

		rs.removeRecipe(rs.findById(r1.getId()).get());
		rs.removeRecipe(rs.findById(r2.getId()).get());
		rs.removeRecipe(rs.findById(r3.getId()).get());
		
		ts.removeTag(t3);
		ts.removeTag(t2);
		ts.removeTag(t1);
		
	}
}
