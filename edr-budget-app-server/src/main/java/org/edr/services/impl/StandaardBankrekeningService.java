package org.edr.services.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.edr.po.Bankrekening;
import org.edr.po.jpa.BankrekeningPO;
import org.edr.po.jpa.BankrekeningPO_;
import org.edr.po.jpa.BoekingPO;
import org.edr.po.jpa.BoekingPO_;
import org.edr.services.BankrekeningService;
import org.edr.util.services.StandaardAbstractService;
import org.springframework.util.Assert;

public class StandaardBankrekeningService extends StandaardAbstractService implements BankrekeningService {

	@Override
	public void createBankrekening(Bankrekening bankrekening) {
		entityManager.persist(bankrekening);
	}

	@Override
	public List<Bankrekening> findBankrekeningen() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Bankrekening> criteriaQueryBankrekening = cb.createQuery(Bankrekening.class);
		Root<BankrekeningPO> bankrekeningFrom = criteriaQueryBankrekening.from(BankrekeningPO.class);
		criteriaQueryBankrekening.select(bankrekeningFrom);
		criteriaQueryBankrekening.orderBy(cb.asc(bankrekeningFrom.get(BankrekeningPO_.rekeningnr)));

		List<Bankrekening> resultList = entityManager.createQuery(criteriaQueryBankrekening).getResultList();

		// Opzoeken van geboekte bedragen
		CriteriaQuery<BoekingPO> criteriaQueryBoeking = cb.createQuery(BoekingPO.class);
		Root<BoekingPO> boekingFrom = criteriaQueryBoeking.from(BoekingPO.class);
		criteriaQueryBoeking.multiselect(boekingFrom.get(BoekingPO_.bankrekening),
				cb.sum(boekingFrom.get(BoekingPO_.bedrag)));
		criteriaQueryBoeking.groupBy(boekingFrom.get(BoekingPO_.bankrekening));

		entityManager.createQuery(criteriaQueryBoeking).getResultList().stream().forEach(s -> {
			boolean found = false;
			for (int i = 0; i < resultList.size(); i++) {
				if (resultList.get(0).getId().equals(s.getBankrekening().getId())) {
					resultList.get(0).setSaldo(s.getBedrag());
					found = true;
					break;
				}
			}
			Assert.isTrue(found);
		});

		return resultList;
	}

	@Override
	public void updateBankrekening(Bankrekening bankrekening) {
		entityManager.merge(bankrekening);
	}

	@Override
	public void deleteBankrekening(Long id) {
		entityManager.remove(entityManager.find(BankrekeningPO.class, id));
	}

}
