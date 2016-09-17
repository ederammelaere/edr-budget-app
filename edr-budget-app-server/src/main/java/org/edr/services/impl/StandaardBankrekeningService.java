package org.edr.services.impl;

import org.edr.po.Bankrekening;
import org.edr.po.jpa.BankrekeningPO;
import org.edr.po.jpa.BankrekeningPO_;
import org.edr.po.jpa.BoekingPO;
import org.edr.po.jpa.BoekingPO_;
import org.edr.services.BankrekeningService;
import org.edr.util.services.StandaardAbstractService;
import org.springframework.util.Assert;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class StandaardBankrekeningService extends StandaardAbstractService implements BankrekeningService {

    @Override
    public void createBankrekening(Bankrekening bankrekening) {
        entityManager.persist(bankrekening);
    }

    @Override
    public List<Bankrekening> findBankrekeningen() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Tuple> criteriaQueryBankrekening = cb.createTupleQuery();
        Root<BankrekeningPO> bankrekeningFrom = criteriaQueryBankrekening.from(BankrekeningPO.class);

        Subquery<BigDecimal> subquery = criteriaQueryBankrekening.subquery(BigDecimal.class);
        Root<BoekingPO> boekingFrom = subquery.from(BoekingPO.class);
        subquery.where(cb.equal(bankrekeningFrom, boekingFrom.get(BoekingPO_.bankrekening)));
        subquery.select(cb.sum(boekingFrom.get(BoekingPO_.bedrag)));

        criteriaQueryBankrekening.select(cb.tuple(bankrekeningFrom.alias("bankrek"),
                subquery.getSelection().alias("saldo")));
        criteriaQueryBankrekening.orderBy(cb.asc(bankrekeningFrom.get(BankrekeningPO_.rekeningnr)));

        return entityManager.createQuery(criteriaQueryBankrekening).getResultList().stream().map(s -> {

            Bankrekening bankrekening = (Bankrekening) s.get("bankrek");
            BigDecimal saldo = (BigDecimal)s.get("saldo");
            bankrekening.setSaldo(saldo == null ? BigDecimal.ZERO : saldo);

            return bankrekening;
        }).collect(Collectors.toList());
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
