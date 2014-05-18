package org.edr.services;

import java.util.List;

import org.edr.domain.BudgetStaat;

public interface BudgetStaatService {

	BudgetStaat getBudgetStaat(int jaar);

	List<BudgetStaat> getBudgetStaatAsList(int jaar);

}
