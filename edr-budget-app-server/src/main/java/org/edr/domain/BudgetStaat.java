package org.edr.domain;

import java.math.BigDecimal;
import java.util.List;

import org.edr.po.Boekrekening;

public interface BudgetStaat {

	Boekrekening getBoekrekening();

	BigDecimal getGeboektBedrag();

	BigDecimal getReferentieBedrag();

	BigDecimal getReferentieJaarBedrag();

	void addGeboektBedrag(BigDecimal geboektBedrag);

	void addReferentieBedrag(BigDecimal referentieBedrag);

	void addReferentieJaarBedrag(BigDecimal referentieJaarBedrag);

	void addChildBudgetStaat(BudgetStaat childBudget);

	List<BudgetStaat> getChildBudgetStaten();

	BudgetStaat getParentBudgetStaat();

	boolean isChild(BudgetStaat budgetStaat);

	boolean isParent(BudgetStaat budgetStaat);

}