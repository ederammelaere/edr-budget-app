package org.edr.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.edr.domain.BudgetStaat;
import org.edr.domain.impl.BudgetStaatDO;
import org.edr.po.Boeking;
import org.edr.po.Boekrekening;
import org.edr.po.Budget;
import org.edr.services.BudgetStaatService;
import org.edr.util.domain.RekeningStelselUtil;
import org.edr.util.services.StandaardAbstractService;

public class StandaardBudgetStaatService extends StandaardAbstractService implements BudgetStaatService {

	@SuppressWarnings("unchecked")
	@Override
	public BudgetStaat getBudgetStaat(int jaar) {
		Map<Boekrekening, BudgetStaat> budgetStaatMap = new HashMap<>();

		//
		// Vormen van rekeningenstelsel
		//

		List<Boekrekening> boekRekeningen = (List<Boekrekening>) entityManager.createQuery(
				"select p from BoekrekeningPO p order by p.rekeningnr").getResultList();

		if (boekRekeningen.size() == 0) {
			return null;
		}

		BudgetStaat rootBudgetStaat = new BudgetStaatDO(boekRekeningen.get(0), null);
		budgetStaatMap.put(boekRekeningen.get(0), rootBudgetStaat);

		BudgetStaat previousBudgetStaat = null;
		for (Boekrekening boekrekening : boekRekeningen) {
			if (previousBudgetStaat == null) {
				previousBudgetStaat = rootBudgetStaat;
			} else {
				while (!RekeningStelselUtil.isChild(boekrekening.getRekeningnr(), previousBudgetStaat.getBoekrekening()
						.getRekeningnr())) {
					previousBudgetStaat = previousBudgetStaat.getParentBudgetStaat();
				}
				previousBudgetStaat = new BudgetStaatDO(boekrekening, previousBudgetStaat);
				budgetStaatMap.put(boekrekening, previousBudgetStaat);
			}
		}

		//
		// Inlezen van budget
		//
		((List<Budget>) entityManager.createQuery("select p from BudgetPO p where jaar = :jaar")
				.setParameter("jaar", jaar).getResultList()).stream().forEach(s -> {
			budgetStaatMap.get(s.getBoekrekening()).addGebudgetteerdBedrag(s.getBedrag());
		});

		//
		// Inlezen van boekingen
		//
		((List<Boeking>) entityManager.createQuery("select p from BoekingPO p where year(datum) = :jaar")
				.setParameter("jaar", jaar).getResultList()).stream().forEach(s -> {
			budgetStaatMap.get(s.getBoekrekening()).addGeboektBedrag(s.getBedrag());
		});
		return rootBudgetStaat;
	}
}
