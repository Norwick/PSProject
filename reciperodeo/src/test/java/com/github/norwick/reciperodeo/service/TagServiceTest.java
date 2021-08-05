package com.github.norwick.reciperodeo.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.norwick.reciperodeo.domain.Tag;

/**
 * Service to interact with Tags
 * @author Norwick Lee
 *
 */
@SpringBootTest
class TagServiceTest {

	@Autowired
	private TagService ts;
	
	//bad code
	@Test
	void testEverythingBasic() {
		long count = ts.count();
		Tag t = new Tag("Spicy");
		ts.saveTag(t);
		Assertions.assertEquals(count + 1, ts.count());
		List<Tag> tl = ts.findByNameContaining("ic");
		int id = tl.get(0).getId();
		Assertions.assertTrue(ts.existsById(id));
		ts.removeTag(tl.get(0));
		Assertions.assertFalse(ts.existsById(id));
		Assertions.assertEquals(count, ts.count());
	}
	
	@Test
	void testFindByName() {
		String name = "yergle";
		Assertions.assertTrue(ts.findByName(name).isEmpty());
		Tag t = new Tag(name);
		t = ts.saveTag(t);
		Assertions.assertTrue(ts.findByName(name).isPresent());
		ts.removeTag(t);
		Assertions.assertTrue(ts.findByName(name).isEmpty());
	}
	
	@Test
	void testSaveTagUnique() {
		String name = "yergle";
		Tag t = new Tag(name);
		t = ts.saveTag(t);
		Assertions.assertTrue(ts.findByName(name).isPresent());
		Tag t2 = new Tag(name);
		final Tag t_2 = t2;
		Assertions.assertThrows(IllegalArgumentException.class, () -> ts.saveTag(t_2));
		ts.removeTag(t);
		Assertions.assertTrue(ts.findByName(name).isEmpty());
		t2 = ts.saveTag(t_2);
		Assertions.assertTrue(ts.findByName(name).isPresent());
		final Tag t_1 = t;
		Assertions.assertThrows(IllegalArgumentException.class, () -> ts.saveTag(t_1));
		ts.removeTag(t2);
		Assertions.assertTrue(ts.findByName(name).isEmpty());
	}
}
