package org.edr.services;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.edr.domain.BudgetStaat;
import org.edr.junit.AbstractJunitTest;
import org.edr.po.Boekrekening;
import org.edr.po.jpa.BoekrekeningPO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BudgetStaatServiceTest extends AbstractJunitTest {

	@Autowired
	private BudgetStaatService budgetStaatService;

	@Test
	public void test1() {
		BudgetStaat rootBudgetStaat = budgetStaatService.getBudgetStaat(2014, 2013);
		assertEquals("000000", rootBudgetStaat.getBoekrekening().getRekeningnr());
		assertEquals(new BigDecimal("45.00"), rootBudgetStaat.getGeboektBedrag());
		assertEquals(new BigDecimal("40.70"), rootBudgetStaat.getReferentieBedrag());
		assertEquals(new BigDecimal("63.00"), rootBudgetStaat.getReferentieJaarBedrag());

		assertEquals(2, rootBudgetStaat.getChildBudgetStaten().size());
		assertEquals("100000", rootBudgetStaat.getChildBudgetStaten().get(0).getBoekrekening().getRekeningnr());
		assertEquals(new BigDecimal("4.75"), rootBudgetStaat.getChildBudgetStaten().get(0).getGeboektBedrag());
		assertEquals(BigDecimal.ZERO, rootBudgetStaat.getChildBudgetStaten().get(0).getReferentieBedrag());
		assertEquals(BigDecimal.ZERO, rootBudgetStaat.getChildBudgetStaten().get(0).getReferentieJaarBedrag());

		assertEquals("200000", rootBudgetStaat.getChildBudgetStaten().get(1).getBoekrekening().getRekeningnr());
		assertEquals(new BigDecimal("40.25"), rootBudgetStaat.getChildBudgetStaten().get(1).getGeboektBedrag());
		assertEquals(new BigDecimal("40.70"), rootBudgetStaat.getChildBudgetStaten().get(1).getReferentieBedrag());
		assertEquals(new BigDecimal("63.00"), rootBudgetStaat.getChildBudgetStaten().get(1).getReferentieJaarBedrag());
	}

	@Test(expected = RuntimeException.class)
	public void test2() {
		Boekrekening boekrekening = entityManager.find(BoekrekeningPO.class, Long.valueOf(2l));
		entityManager.remove(boekrekening);
		entityManager.flush();
		budgetStaatService.getBudgetStaat(2014, 2013);
	}

	@Test(expected = NullPointerException.class)
	public void test3() {
		Boekrekening boekrekening = entityManager.find(BoekrekeningPO.class, Long.valueOf(3l));
		entityManager.remove(boekrekening);
		entityManager.flush();
		budgetStaatService.getBudgetStaat(2014, 2013);
	}

	@Test
	public void test4() {
		List<BudgetStaat> budgetStaatList = budgetStaatService.getBudgetStaatAsList(2014, 2013);

		assertEquals(6, budgetStaatList.size());

		assertEquals("000000", budgetStaatList.get(0).getBoekrekening().getRekeningnr());
		assertEquals("100000", budgetStaatList.get(1).getBoekrekening().getRekeningnr());
		assertEquals("110000", budgetStaatList.get(2).getBoekrekening().getRekeningnr());
		assertEquals("200000", budgetStaatList.get(3).getBoekrekening().getRekeningnr());
		assertEquals("220000", budgetStaatList.get(4).getBoekrekening().getRekeningnr());
		assertEquals("220100", budgetStaatList.get(5).getBoekrekening().getRekeningnr());

		assertEquals(new BigDecimal("40.25"), budgetStaatList.get(5).getGeboektBedrag());
		assertEquals(new BigDecimal("40.70"), budgetStaatList.get(0).getReferentieBedrag());
		assertEquals(new BigDecimal("63.00"), budgetStaatList.get(0).getReferentieJaarBedrag());
		assertEquals(BigDecimal.ZERO, budgetStaatList.get(1).getReferentieJaarBedrag());
	}

	@Test
	public void test5() {
        // Door als jaar 2013 te nemen en referentiejaar 2013 bekomen we op basis van de testdata
        // een situatie met 31/12/2013 als uiterste boekingsdatum.
		List<BudgetStaat> budgetStaatList = budgetStaatService.getBudgetStaatAsList(2013, 2013);

		assertEquals(6, budgetStaatList.size());

		assertEquals("000000", budgetStaatList.get(0).getBoekrekening().getRekeningnr());
		assertEquals("100000", budgetStaatList.get(1).getBoekrekening().getRekeningnr());
		assertEquals("110000", budgetStaatList.get(2).getBoekrekening().getRekeningnr());
		assertEquals("200000", budgetStaatList.get(3).getBoekrekening().getRekeningnr());
		assertEquals("220000", budgetStaatList.get(4).getBoekrekening().getRekeningnr());
		assertEquals("220100", budgetStaatList.get(5).getBoekrekening().getRekeningnr());

		assertEquals(new BigDecimal("63.00"), budgetStaatList.get(5).getGeboektBedrag());
		assertEquals(new BigDecimal("63.00"), budgetStaatList.get(0).getReferentieBedrag());
		assertEquals(new BigDecimal("63.00"), budgetStaatList.get(0).getReferentieJaarBedrag());
		assertEquals(BigDecimal.ZERO, budgetStaatList.get(1).getReferentieJaarBedrag());
	}
}
