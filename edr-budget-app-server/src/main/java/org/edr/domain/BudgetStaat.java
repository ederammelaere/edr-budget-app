package org.edr.domain;

import java.math.BigDecimal;
import java.util.List;

import org.edr.po.Boekrekening;

public interface BudgetStaat {

	Boekrekening getBoekrekening();

	BigDecimal getGebudgetteerdBedrag();

	void addGebudgetteerdBedrag(BigDecimal gebudgetteerdBedrag);

	BigDecimal getGeboektBedrag();

	void addGeboektBedrag(BigDecimal geboektBedrag);

	void addChildBudgetStaat(BudgetStaat childBudget);

	List<BudgetStaat> getChildBudgetStaten();

	BudgetStaat getParentBudgetStaat();

	boolean isChild(BudgetStaat budgetStaat);

	boolean isParent(BudgetStaat budgetStaat);

}