package org.edr.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.persistence.PersistenceException;

import org.edr.junit.AbstractJunitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JournaalServiceTest extends AbstractJunitTest {

	@Autowired
	private JournaalService journaalService;

	@Test(expected = NullPointerException.class)
	public void test1() {
		journaalService.loadJournaalFromStream(null);
	}

	@Test(expected = RuntimeException.class)
	public void test2() {
		journaalService.loadJournaalFromStream(openReader("org/edr/samples/empty-file.csv"));
	}

	@Test(expected = RuntimeException.class)
	public void test3() {
		journaalService.loadJournaalFromStream(openReader("org/edr/samples/corrupt-headerline.csv"));
	}

	@Test
	public void test4() {
		journaalService.loadJournaalFromStream(openReader("org/edr/samples/valid-empty-file.csv"));
	}

	@Test
	public void test5() {
		journaalService.loadJournaalFromStream(openReader("org/edr/samples/valid-file.csv"));
		entityManager.flush();
		assertSQL("journaal004", "select * from journaal order by id");
	}

	@Test(expected = PersistenceException.class)
	public void test6() {
		journaalService.loadJournaalFromStream(openReader("org/edr/samples/invalid-rekening-file.csv"));
		entityManager.flush();
	}

	@Test
	public void test7() {
		entityManager.createNativeQuery("delete from journaal").executeUpdate();

		journaalService.loadJournaalFromStream(openReader("org/edr/samples/valid-file.csv"));
		entityManager.flush();
		assertSQL("journaal005", "select * from journaal order by id");
	}

	private BufferedReader openReader(String classpathResource) {
		return new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(classpathResource)));
	}
}
