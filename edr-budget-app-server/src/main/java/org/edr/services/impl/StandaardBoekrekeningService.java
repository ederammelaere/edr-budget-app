package org.edr.services.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.edr.po.Boekrekening;
import org.edr.po.jpa.BoekrekeningPO;
import org.edr.po.jpa.BoekrekeningPO_;
import org.edr.services.BoekrekeningService;
import org.edr.util.services.StandaardAbstractService;

public class StandaardBoekrekeningService extends StandaardAbstractService implements BoekrekeningService {

	@Override
	public List<Boekrekening> findBoekrekeningen() {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Boekrekening> criteriaQueryBoekrekening = cb.createQuery(Boekrekening.class);
		Root<BoekrekeningPO> boekrekeningFrom = criteriaQueryBoekrekening.from(BoekrekeningPO.class);
		criteriaQueryBoekrekening.select(boekrekeningFrom);
		criteriaQueryBoekrekening.orderBy(cb.asc(boekrekeningFrom.get(BoekrekeningPO_.rekeningnr)));

		return entityManager.createQuery(criteriaQueryBoekrekening).getResultList();
	}

}
