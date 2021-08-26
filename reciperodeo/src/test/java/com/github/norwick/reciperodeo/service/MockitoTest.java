package com.github.norwick.reciperodeo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.norwick.reciperodeo.domain.Tag;

/**
 * Not very useful mockito test that demonstrates how to use mockito
 * @author Norwick Lee
 *
 */
@SpringBootTest
class MockitoTest {
	private TagService ts;
	
	/**
	 * sets up the fake tag service
	 */
	@BeforeEach
	public void setUp() {
		Tag t1 = new Tag("spicy");
		Tag t2 = new Tag("sweet");
		Tag t3 = new Tag("savory");
		
		ts = Mockito.mock(TagService.class);
		Mockito.when(ts.findById(Mockito.anyInt())).thenReturn(Optional.empty());	
		Mockito.doReturn(Optional.of(t1)).when(ts).findById(1);
		Mockito.doReturn(Optional.of(t2)).when(ts).findById(2);
		Mockito.doReturn(Optional.of(t3)).when(ts).findById(3);
		
		Mockito.doReturn(3L).when(ts).count();
		
		Mockito.when(ts.existsById(Mockito.anyInt())).thenReturn(false);
		Mockito.doReturn(true).when(ts).existsById(1);
		Mockito.doReturn(true).when(ts).existsById(2);
		Mockito.doReturn(true).when(ts).existsById(3);
		
		Mockito.when(ts.findByName(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.doReturn(Optional.of(t1)).when(ts).findByName("spicy");
		Mockito.doReturn(Optional.of(t2)).when(ts).findByName("savory");
		Mockito.doReturn(Optional.of(t3)).when(ts).findByName("sweet");
		
		List<Tag> ls = new ArrayList<Tag>();
		ls.add(t1);
		ls.add(t2);
		ls.add(t3);
		
		List<Tag> ly = new ArrayList<Tag>();
		ly.add(t2);
		ly.add(t3);
		
		List<Tag> lm = new ArrayList<Tag>();
		
		Mockito.doReturn(ls).when(ts).findByNameContaining("s");
		Mockito.doReturn(ly).when(ts).findByNameContaining("y");
		Mockito.doReturn(lm).when(ts).findByNameContaining("m");
	}
	
	@ParameterizedTest
	@CsvSource({"s,3","y,2","m,0"})
	void testFindByNameContaining(String name, int count) {
		Assertions.assertEquals(count, ts.findByNameContaining(name).size());
	}

	@ParameterizedTest
	@CsvSource({"spicy,true","savory,true","sweet,true","salty,false","sandy,false"})
	void testFindByName(String name, boolean found) {
		Assertions.assertEquals(found, ts.findByName(name).isPresent());
	}

	@ParameterizedTest
	@CsvSource({"1,true","2,true","3,true","4,false"})
	void testFindById(int id, boolean found) {
		Assertions.assertEquals(found, ts.findById(id).isPresent());
	}

	@ParameterizedTest
	@CsvSource({"1,true","2,true","3,true","4,false"})
	void testExistsById(int id, boolean found) {
		Assertions.assertEquals(found, ts.existsById(id));
	}

	@Test
	void testCount() {
		Assertions.assertEquals(3L, ts.count());
	}
}

