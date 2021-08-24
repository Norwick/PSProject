package com.github.norwick.reciperodeo.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.github.norwick.reciperodeo.domain.Theme;

/**
 * Tests for ThemeService
 * @author Norwick Lee
 */
@SpringBootTest
public class ThemeServiceTest {
	
	@Autowired
	ThemeService ts;
	
	@ParameterizedTest
	@CsvSource({"2,Light","1,Green","1,Dark"})
	void saveThemeTest(int id, String name) {
		Theme t = new Theme(id, name);
		ts.saveTheme(t);
		Optional<Theme> ot = ts.findById(t.getId());
		Assertions.assertTrue(ot.isPresent());
		t = ot.get();
		Assertions.assertEquals(id, t.getId());
		Assertions.assertTrue(t.sameName(name));
	}
	
	@Test
	void saveThemeThrowsTest() {
		Theme t = new Theme(3,"Light");
		Assertions.assertThrows(DataIntegrityViolationException.class, () -> {ts.saveTheme(t);});
	}
	
	@ParameterizedTest
	@CsvSource({"2,true","1,true","3,false"})
	void findByIdTest(int id, boolean found) {
		Assertions.assertEquals(found, ts.findById(id).isPresent());
	}
	
	@ParameterizedTest
	@CsvSource({"Light,true","Dark,true","Green,false"})
	void findByNameTest(String name, boolean found) {
		Assertions.assertEquals(found, ts.findByName(name).isPresent());
	}
	
	@Test
	void findNullTest() {
		Assertions.assertThrows(NullPointerException.class, () -> {ts.findByName(null);});
		Assertions.assertThrows(NullPointerException.class, () -> {ts.saveTheme(null);});
		Assertions.assertThrows(NullPointerException.class, () -> {ts.removeTheme(null);});
	}
	
	@Test
	void findAllTest() {
		Assertions.assertEquals(2, ts.findAll().size());
	}
	
	@ParameterizedTest
	@ValueSource(ints= {2,1})
	void removeThemeTest(int id) {
		Optional<Theme> ot = ts.findById(id);
		Assertions.assertTrue(ot.isPresent());
		Theme t = ot.get();
		ts.removeTheme(t);
		Assertions.assertTrue(ts.findById(id).isEmpty());
	}
}
