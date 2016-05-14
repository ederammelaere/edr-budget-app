package org.edr.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.edr.domain.BudgetStaat;
import org.edr.domain.impl.BudgetStaatDO;
import org.edr.po.Boekrekening;
import org.edr.po.jpa.BoekingPO;
import org.edr.po.jpa.BoekingPO_;
import org.edr.po.jpa.BoekrekeningPO;
import org.edr.po.jpa.BoekrekeningPO_;
import org.edr.services.BudgetStaatService;
import org.edr.util.domain.RekeningStelselUtil;
import org.edr.util.services.StandaardAbstractService;

public class StandaardBudgetStaatService extends StandaardAbstractService implements BudgetStaatService {

	@Override
	public BudgetStaat getBudgetStaat(int jaar) {
		Map<Boekrekening, BudgetStaat> budgetStaatMap = new HashMap<>();

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		//
		// Vormen van rekeningenstelsel
		//

		CriteriaQuery<Boekrekening> criteriaQueryBoekrekening = cb.createQuery(Boekrekening.class);

		Root<BoekrekeningPO> boekrekeningFrom = criteriaQueryBoekrekening.from(BoekrekeningPO.class);
		criteriaQueryBoekrekening.select(boekrekeningFrom);
		criteriaQueryBoekrekening.orderBy(cb.asc(boekrekeningFrom.get(BoekrekeningPO_.rekeningnr)));

		List<Boekrekening> boekRekeningen = entityManager.createQuery(criteriaQueryBoekrekening).getResultList();

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
		// Inlezen van boekingen
		//

		CriteriaQuery<Tuple> criteriaQueryBoeking = cb.createTupleQuery();

		Root<BoekingPO> boekingFrom = criteriaQueryBoeking.from(BoekingPO.class);
		criteriaQueryBoeking.select(cb.tuple(boekingFrom.get(BoekingPO_.boekrekening),
				cb.sum(boekingFrom.get(BoekingPO_.bedrag))));
		criteriaQueryBoeking
				.where(cb.equal(cb.function("year", Integer.class, boekingFrom.get(BoekingPO_.datum)), jaar));
		criteriaQueryBoeking.groupBy(boekingFrom.get(BoekingPO_.boekrekening));

		entityManager.createQuery(criteriaQueryBoeking).getResultList().forEach(s -> {
			budgetStaatMap.get(s.get(0)).addGeboektBedrag((BigDecimal) s.get(1));
			;
		});

		return rootBudgetStaat;
	}

	@Override
	public List<BudgetStaat> getBudgetStaatAsList(int jaar) {
		List<BudgetStaat> budgetStaatList = new ArrayList<>();
		BudgetStaat rootBudgetStaat = getBudgetStaat(jaar);
		if (rootBudgetStaat != null) {
			budgetStaatList.add(rootBudgetStaat);
			walkBudgetStaat(rootBudgetStaat, budgetStaatList);
		}
		return budgetStaatList;
	}

	private void walkBudgetStaat(BudgetStaat budgetStaat, List<BudgetStaat> budgetStaatList) {
		for (BudgetStaat childBudgetStaat : budgetStaat.getChildBudgetStaten()) {
			budgetStaatList.add(childBudgetStaat);
			walkBudgetStaat(childBudgetStaat, budgetStaatList);
		}
	}

}
