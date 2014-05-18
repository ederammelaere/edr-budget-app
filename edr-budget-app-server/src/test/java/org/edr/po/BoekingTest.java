package org.edr.po;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.edr.junit.AbstractJunitTest;
import org.edr.po.jpa.BankrekeningPO;
import org.edr.po.jpa.BoekingPO;
import org.edr.po.jpa.BoekrekeningPO;
import org.junit.Test;

public class BoekingTest extends AbstractJunitTest {

	@Test
	public void testFind() {
		Boeking boeking = entityManager.find(BoekingPO.class, Long.valueOf(1l));

		assertEquals("sigaretten", boeking.getOmschrijving());
		assertEquals(new BigDecimal("4.75"), boeking.getBedrag());
		assertEquals(LocalDate.of(2014, 4, 24), boeking.getDatum());
		assertEquals("test-rekening", boeking.getBankrekening().getOmschrijving());
		assertEquals("cash uitgaven", boeking.getBoekrekening().getOmschrijving());
	}

	@Test
	public void testPersist() {
		// Je moet op deze transient object zowel id als version invullen
		Bankrekening bankrekening = new BankrekeningPO();
		bankrekening.setId(Long.valueOf(2l));
		bankrekening.setVersion(Integer.valueOf(1));
		Boekrekening boekrekening = new BoekrekeningPO();
		boekrekening.setId(Long.valueOf(1l));
		boekrekening.setVersion(Integer.valueOf(1));

		Boeking boeking = new BoekingPO();
		boeking.setBankrekening(bankrekening);
		boeking.setBoekrekening(boekrekening);
		boeking.setDatum(LocalDate.of(2014, 4, 25));
		boeking.setOmschrijving("test");
		boeking.setBedrag(new BigDecimal("3.70"));

		entityManager.persist(boeking);
		entityManager.flush();

		assertSQL("boeking003", "select * from boeking where id=10");
	}

}
