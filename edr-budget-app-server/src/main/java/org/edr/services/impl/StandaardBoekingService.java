package org.edr.services.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.edr.po.Boeking;
import org.edr.po.Journaal;
import org.edr.po.jpa.BankrekeningPO;
import org.edr.po.jpa.BoekingPO;
import org.edr.po.jpa.BoekingPO_;
import org.edr.po.jpa.BoekrekeningPO;
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
		boeking.setBankrekening(entityManager.find(BankrekeningPO.class, boeking.getBankrekening().getId()));
		boeking.setBoekrekening(entityManager.find(BoekrekeningPO.class, boeking.getBoekrekening().getId()));
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

	@Override
	public void saveBoekingen(Journaal journaal) {
		BigDecimal bedrag = new BigDecimal(0.00);
		for (Boeking boeking : journaal.getBoekingen()) {
			bedrag = bedrag.add(boeking.getBedrag());
		}

		boolean transferBoeking = (bedrag.compareTo(BigDecimal.ZERO) == 0 && journaal.getBoekingen().size() == 2);

		if (!bedrag.equals(journaal.getBedrag()) && !transferBoeking) {
			throw new IllegalArgumentException("Bedrag komt niet overeen");
		}

		findBoekingen(journaal).stream().forEach(s -> {
			entityManager.remove(s);
		});

		if (!transferBoeking) {
			for (Boeking boeking : journaal.getBoekingen()) {
				boeking.setId(null);
				boeking.setVersion(0);
				boeking.setJournaal(journaal);
				boeking.setBankrekening(journaal.getBankrekening());
				boeking.setDatum(journaal.getDatum());
				createBoeking(boeking);
			}
		} else {
			for (Boeking boeking : journaal.getBoekingen()) {
				boeking.setId(null);
				boeking.setVersion(0);
				boeking.setJournaal(journaal);
				boeking.setDatum(journaal.getDatum());
				createBoeking(boeking);
				if (boeking.getBedrag().abs().compareTo(journaal.getBedrag().abs()) != 0) {
					throw new IllegalArgumentException("Ongeldige transfer");
				}
			}
		}
	}

	public List<Boeking> findBoekingen(Journaal journaal) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Boeking> criteriaQueryBoeking = cb.createQuery(Boeking.class);
		Root<BoekingPO> boekingFrom = criteriaQueryBoeking.from(BoekingPO.class);
		criteriaQueryBoeking.select(boekingFrom);
		criteriaQueryBoeking.where(cb.equal(boekingFrom.get(BoekingPO_.journaal), journaal));

		return entityManager.createQuery(criteriaQueryBoeking).getResultList();
	}
}
