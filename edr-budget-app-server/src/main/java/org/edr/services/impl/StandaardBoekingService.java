package org.edr.services.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.edr.po.Boeking;
import org.edr.po.jpa.BoekingPO;
import org.edr.po.jpa.BoekingPO_;
import org.edr.services.BoekingService;
import org.edr.util.services.StandaardAbstractService;

public class StandaardBoekingService extends StandaardAbstractService implements BoekingService {

	@Override
	public List<Boeking> findBoekingen(int jaar) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Boeking> criteriaQueryBoeking = cb.createQuery(Boeking.class);
		Root<BoekingPO> boekingFrom = criteriaQueryBoeking.from(BoekingPO.class);
		criteriaQueryBoeking.select(boekingFrom);
		boekingFrom.fetch(BoekingPO_.bankrekening);
		boekingFrom.fetch(BoekingPO_.boekrekening);
		criteriaQueryBoeking
				.where(cb.equal(cb.function("year", Integer.class, boekingFrom.get(BoekingPO_.datum)), jaar));
		criteriaQueryBoeking.orderBy(cb.asc(boekingFrom.get(BoekingPO_.datum)), cb.asc(boekingFrom.get(BoekingPO_.id)));

		return entityManager.createQuery(criteriaQueryBoeking).getResultList();
	}

	@Override
	public void createBoeking(Boeking boeking) {
		entityManager.persist(boeking);
	}

	@Override
	public void deleteBoeking(Long id) {
		entityManager.remove(entityManager.find(BoekingPO.class, id));
	}

	@Override
	public void updateBoeking(Boeking boeking) {
		entityManager.merge(boeking);
	}
}
