package org.edr.services.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.edr.po.Budget;
import org.edr.po.jpa.BoekrekeningPO;
import org.edr.po.jpa.BudgetPO;
import org.edr.po.jpa.BudgetPO_;
import org.edr.services.BudgetService;
import org.edr.util.services.StandaardAbstractService;

public class StandaardBudgetService extends StandaardAbstractService implements BudgetService {

	@Override
	public List<Budget> findBudgetten(int jaar) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Budget> criteriaQueryBudget = cb.createQuery(Budget.class);
		Root<BudgetPO> budgetFrom = criteriaQueryBudget.from(BudgetPO.class);
		criteriaQueryBudget.select(budgetFrom);
		budgetFrom.fetch(BudgetPO_.boekrekening);
		criteriaQueryBudget.where(cb.equal(budgetFrom.get(BudgetPO_.jaar), jaar));
		criteriaQueryBudget.orderBy(cb.asc(budgetFrom.get(BudgetPO_.boekrekening)));

		return entityManager.createQuery(criteriaQueryBudget).getResultList();
	}

	@Override
	public void createBudget(Budget budget) {
		budget.setBoekrekening(entityManager.find(BoekrekeningPO.class, budget.getBoekrekening().getId()));
		entityManager.persist(budget);
	}

	@Override
	public void deleteBudget(Long id) {
		entityManager.remove(entityManager.find(BudgetPO.class, id));
	}

	@Override
	public void updateBudget(Budget budget) {
		entityManager.merge(budget);
	}
}
