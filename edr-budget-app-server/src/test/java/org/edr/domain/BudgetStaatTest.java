package org.edr.domain;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.edr.domain.impl.BudgetStaatDO;
import org.edr.po.Boekrekening;
import org.edr.po.jpa.BoekrekeningPO;
import org.junit.Test;

public class BudgetStaatTest {

	private long id = 1;

	@Test
	public void testRootValid() {
		getRootBudgetStaat();
	}

	@Test(expected = RuntimeException.class)
	public void testRootInvalid() {
		new BudgetStaatDO(getBoekrekening("100000"), null);
	}

	@Test
	public void testValid() {
		BudgetStaat rootBudgetStaat = getRootBudgetStaat();
		BudgetStaat b100000 = new BudgetStaatDO(getBoekrekening("100000"), rootBudgetStaat);
		BudgetStaat b200000 = new BudgetStaatDO(getBoekrekening("200000"), rootBudgetStaat);
		BudgetStaat b110000 = new BudgetStaatDO(getBoekrekening("110000"), b100000);
		BudgetStaat b110300 = new BudgetStaatDO(getBoekrekening("110300"), b110000);
		BudgetStaat b110301 = new BudgetStaatDO(getBoekrekening("110301"), b110300);
		BudgetStaat b110302 = new BudgetStaatDO(getBoekrekening("110302"), b110300);

		assertSame(b100000, rootBudgetStaat.getChildBudgetStaten().get(0));
		assertSame(b200000, rootBudgetStaat.getChildBudgetStaten().get(1));
		assertSame(b110000, b100000.getChildBudgetStaten().get(0));
		assertSame(b110300, b110000.getChildBudgetStaten().get(0));
		assertSame(b110301, b110300.getChildBudgetStaten().get(0));
		assertSame(b110302, b110300.getChildBudgetStaten().get(1));

		assertEquals(2, rootBudgetStaat.getChildBudgetStaten().size());
		assertEquals(1, b100000.getChildBudgetStaten().size());
		assertEquals(0, b200000.getChildBudgetStaten().size());
		assertEquals(1, b110000.getChildBudgetStaten().size());
		assertEquals(2, b110300.getChildBudgetStaten().size());
		assertEquals(0, b110301.getChildBudgetStaten().size());
		assertEquals(0, b110302.getChildBudgetStaten().size());

		assertSame(rootBudgetStaat, b100000.getParentBudgetStaat());
		assertSame(b100000, b110000.getParentBudgetStaat());
		assertSame(b110000, b110300.getParentBudgetStaat());
		assertSame(b110300, b110301.getParentBudgetStaat());
		assertSame(b110300, b110302.getParentBudgetStaat());

		b110301.addGeboektBedrag(new BigDecimal("10.05"));
		b110302.addGeboektBedrag(new BigDecimal("1.05"));

		b110301.addGebudgetteerdBedrag(new BigDecimal("100.0"));
		b110302.addGebudgetteerdBedrag(new BigDecimal("10.0"));

		b200000.addGeboektBedrag(new BigDecimal("120.51"));
		b200000.addGebudgetteerdBedrag(new BigDecimal("90.0"));

		assertEquals(new BigDecimal("131.61"), rootBudgetStaat.getGeboektBedrag());
		assertEquals(new BigDecimal("200.0"), rootBudgetStaat.getGebudgetteerdBedrag());

		assertEquals(new BigDecimal("11.10"), b100000.getGeboektBedrag());
		assertEquals(new BigDecimal("110.0"), b100000.getGebudgetteerdBedrag());

		assertEquals(new BigDecimal("120.51"), b200000.getGeboektBedrag());
		assertEquals(new BigDecimal("90.0"), b200000.getGebudgetteerdBedrag());

		assertEquals(new BigDecimal("11.10"), b110000.getGeboektBedrag());
		assertEquals(new BigDecimal("110.0"), b110000.getGebudgetteerdBedrag());

		assertEquals(new BigDecimal("11.10"), b110300.getGeboektBedrag());
		assertEquals(new BigDecimal("110.0"), b110300.getGebudgetteerdBedrag());

		assertEquals(new BigDecimal("10.05"), b110301.getGeboektBedrag());
		assertEquals(new BigDecimal("100.0"), b110301.getGebudgetteerdBedrag());

		assertEquals(new BigDecimal("1.05"), b110302.getGeboektBedrag());
		assertEquals(new BigDecimal("10.0"), b110302.getGebudgetteerdBedrag());

	}

	@Test(expected = RuntimeException.class)
	public void testInvalidA() {
		new BudgetStaatDO(getBoekrekening("110000"), getRootBudgetStaat());
	}

	@Test(expected = RuntimeException.class)
	public void testInvalidB() {
		BudgetStaat bs1 = new BudgetStaatDO(getBoekrekening("100000"), getRootBudgetStaat());
		BudgetStaat bs2 = new BudgetStaatDO(getBoekrekening("100000"), getRootBudgetStaat());

		bs1.isChild(bs2);
	}

	private BudgetStaat getRootBudgetStaat() {
		return new BudgetStaatDO(getBoekrekening("000000"), null);
	}

	private Boekrekening getBoekrekening(String rekeningnr) {
		Boekrekening boekrekening = new BoekrekeningPO();
		boekrekening.setId(id++);
		boekrekening.setRekeningnr(rekeningnr);

		return boekrekening;
	}

}
