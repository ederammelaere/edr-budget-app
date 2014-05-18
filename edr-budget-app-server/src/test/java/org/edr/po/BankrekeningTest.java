package org.edr.po;

import static org.junit.Assert.*;

import org.edr.junit.AbstractJunitTest;
import org.edr.po.jpa.BankrekeningPO;
import org.junit.Test;

public class BankrekeningTest extends AbstractJunitTest {

	@Test
	public void testToEasy() {
		Bankrekening bankrekening = new BankrekeningPO();
		bankrekening.setOmschrijving("Hello world!");
		assertEquals("Dit is niet mogelijk", "Hello world!", bankrekening.getOmschrijving());
	}

	@Test
	public void testFindNothing() {
		assertNull(entityManager.find(BankrekeningPO.class, Long.valueOf(2706l)));
	}

	@Test
	public void testFindSomething() {
		Bankrekening bankrekening = entityManager.find(BankrekeningPO.class, Long.valueOf(1l));
		assertNotNull(bankrekening);
		assertEquals(Long.valueOf(1l), bankrekening.getId());
		assertEquals(Integer.valueOf(0), bankrekening.getVersion());
		assertEquals("test-rekening", bankrekening.getOmschrijving());
	}

	@Test
	public void testPersist() {
		Bankrekening bankRekening = new BankrekeningPO();
		bankRekening.setOmschrijving("zichtrekening");
		bankRekening.setRekeningnr("BE02 256 66");
		entityManager.persist(bankRekening);
		entityManager.flush();

		assertEquals(Long.valueOf(10l), bankRekening.getId());
		assertSQL("bankrekening001", "select * from bankrekening where id=10");
		assertSQL("nextkey001", "select * from nextkey where entityname='bankrekening'");
	}

	@Test
	public void testUpdate() {
		Bankrekening bankrekening = entityManager.find(BankrekeningPO.class, Long.valueOf(1l));
		bankrekening.setOmschrijving("Bijgewerkte omschrijving");
		entityManager.flush();

		assertSQL("bankrekening002", "select * from bankrekening where id=1");
	}

}
