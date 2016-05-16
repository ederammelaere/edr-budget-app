package org.edr.services;

import java.util.List;

import org.edr.domain.BudgetStaat;

public interface BudgetStaatService {

	BudgetStaat getBudgetStaat(int jaar, int referentieJaar);

	List<BudgetStaat> getBudgetStaatAsList(int jaar, int referentieJaar);

}
