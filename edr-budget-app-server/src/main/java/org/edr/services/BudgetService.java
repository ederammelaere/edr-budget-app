package org.edr.services;

import java.util.List;

import org.edr.po.Budget;

public interface BudgetService {

	void createBudget(Budget Budget);

	List<Budget> findBudgetten(int jaar);

	void updateBudget(Budget Budget);

	void deleteBudget(Long id);

}
