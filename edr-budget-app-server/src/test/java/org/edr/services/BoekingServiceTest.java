package org.edr.services;

import org.edr.junit.AbstractJunitTest;
import org.edr.po.Boeking;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BoekingServiceTest extends AbstractJunitTest {

	@Autowired
	private BoekingService boekingService;

	@Test
	public void testFindManueleBoekingen() {
		List<Boeking> boekingen;

		boekingen = boekingService.findManueleBoekingen(LocalDate.of(2015,1,1));
		assertEquals(0, boekingen.size());

        boekingen = boekingService.findManueleBoekingen(LocalDate.of(2012,6,3));
        assertEquals(0, boekingen.size());

        boekingen = boekingService.findManueleBoekingen(LocalDate.of(2014,4,24));
        assertEquals(1, boekingen.size());
        assertEquals(1l, boekingen.get(0).getId().longValue());
	}

}
