package org.edr.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.edr.po.Bankrekening;
import org.edr.po.Boeking;
import org.edr.po.Journaal;
import org.edr.po.jpa.BoekingPO;
import org.edr.po.jpa.BoekingPO_;
import org.edr.po.jpa.JournaalPO;
import org.edr.po.jpa.JournaalPO_;
import org.edr.services.JournaalService;
import org.edr.util.services.StandaardAbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StandaardJournaalService extends StandaardAbstractService implements JournaalService {

    private static final Logger logger = LoggerFactory.getLogger(StandaardJournaalService.class);

    @Override
    public List<Journaal> findJournaal(int jaar) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Journaal> criteriaQueryJournaal = cb.createQuery(Journaal.class);
        Root<JournaalPO> journaalFrom = criteriaQueryJournaal.from(JournaalPO.class);
        criteriaQueryJournaal.select(journaalFrom);
        criteriaQueryJournaal.distinct(true);
        journaalFrom.fetch(JournaalPO_.bankrekening);
        journaalFrom.fetch(JournaalPO_.boekingen, JoinType.LEFT);
        criteriaQueryJournaal.where(cb.equal(cb.function("year", Integer.class, journaalFrom.get(JournaalPO_.datum)),
                jaar));
        criteriaQueryJournaal.orderBy(cb.asc(journaalFrom.get(JournaalPO_.datum)),
                cb.asc(journaalFrom.get(JournaalPO_.id)));

        return entityManager.createQuery(criteriaQueryJournaal).getResultList();
    }

    @Override
    public void loadJournaalFromStream(BufferedReader reader) {
        // Ophalen van de rekeningen
        List<Bankrekening> rekeningenList = entityManager.createQuery("from BankrekeningPO", Bankrekening.class)
                .getResultList();
        Map<String, Bankrekening> rekeningen = new HashMap<>();
        for (Bankrekening rekening : rekeningenList) {
            rekeningen.put(rekening.getRekeningnr(), rekening);
        }

        // Ophalen van maximum datum en afschriftnummer
        LocalDate maxDatum = getMaxDatum();
        Integer maxAfschriftnummer = getMaxAfschriftnummer(maxDatum);

        logger.info("maxDatum = " + maxDatum + "; maxAfschriftnummer = " + maxAfschriftnummer);

        // Evalueren van header lijn
        CheckFirstlineFilter firstlineFilter = new CheckFirstlineFilter();

        // Pattern die spaties zal reduceren in de transactie-String
        Pattern patternSpatiesReduceren = Pattern.compile("  +");

        // Doorlopen van bestand
        reader.lines()
                .skip(12)
                .filter(firstlineFilter)
                .map(s -> s.split(";", -1))
                .map(IntermediairJournaal::new)
                .map(s -> {
                    Journaal journaal = new JournaalPO();

                    // Transactie => eerst spaties reduceren, en zo nodig nog
                    // afkappen op 250 posities
                    String transactie = s.getTransactie();
                    Matcher matcherSpatiesReduceren = patternSpatiesReduceren.matcher(transactie);
                    transactie = matcherSpatiesReduceren.replaceAll(" ");
                    transactie = StringUtils.abbreviate(transactie, 250);

                    journaal.setBankrekening(rekeningen.get(s.getRekening()));
                    journaal.setDatum(s.getBoekingsdatum());
                    journaal.setAfschriftnummer(s.getAfschriftnummer());
                    journaal.setTransactienummer(s.getTransactienummer());
                    journaal.setTegenpartijRekening(s.getTegenpartijRekenening());
                    journaal.setTegenpartijEigenRekening(rekeningen.get(s.getTegenpartijRekenening()));
                    journaal.setTegenpartijNaam(s.getTegenpartijNaam());
                    journaal.setTegenpartijAdres(s.getTegenpartijAdres());
                    journaal.setTegenpartijPlaats(s.getTegenpartijPlaats());
                    journaal.setTransactie(transactie);
                    journaal.setValutadatum(s.getValutadatum());
                    journaal.setBedrag(s.getBedrag());
                    journaal.setDevies(s.getDevies());
                    journaal.setBic(s.getBic());
                    journaal.setLandcode(s.getLandcode());

                    return journaal;
                })
                .filter(s -> s.getAfschriftnummer() > 0
                        && (maxDatum == null || s.getDatum().compareTo(maxDatum) > 0 || (s.getDatum().equals(maxDatum) && s
                        .getAfschriftnummer() > maxAfschriftnummer))).forEach(s -> {
                    if (!s.getLandcode().equals("BE") && !s.getLandcode().isEmpty() && !s.getLandcode().equals("00")) {
                        throw new RuntimeException("Onverwachte landcode " + s.getLandcode());
                    }
                    if (!s.getDevies().equals("EUR")) {
                        throw new RuntimeException("Onverwachte munt " + s.getDevies());
                    }

                    entityManager.persist(s);
                });

        if (firstlineFilter.isFirstline()) {
            throw new RuntimeException("Er werd geen header lijn gevonden");
        }
    }

    private Integer getMaxAfschriftnummer(LocalDate maxDatum) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
        Root<JournaalPO> from = query.from(JournaalPO.class);
        query.select(cb.max(from.get(JournaalPO_.afschriftnummer)));
        query.where(cb.equal(from.get(JournaalPO_.datum), maxDatum));

        return entityManager.createQuery(query).getSingleResult();
    }

    private LocalDate getMaxDatum() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LocalDate> query = cb.createQuery(LocalDate.class);
        Root<JournaalPO> from = query.from(JournaalPO.class);
        query.select(cb.greatest(from.get(JournaalPO_.datum)));

        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<Boeking> findPreviousBoekingen(Long journaalId) {
        Journaal nieuwJournaal = entityManager.find(JournaalPO.class, journaalId);
        Long previousJournaalid = null;

        if (!nieuwJournaal.getTegenpartijRekening().trim().isEmpty()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            CriteriaQuery<Long> criteriaQueryPreviousJournaalid = cb.createQuery(Long.class);
            Root<JournaalPO> rootJournaal = criteriaQueryPreviousJournaalid.from(JournaalPO.class);
            criteriaQueryPreviousJournaalid.select(cb.max(rootJournaal.get(JournaalPO_.id)));
            criteriaQueryPreviousJournaalid.where(
                    cb.and(
                            cb.equal(rootJournaal.get(JournaalPO_.tegenpartijRekening),
                                    nieuwJournaal.getTegenpartijRekening()),
                            cb.isNotEmpty(rootJournaal.get(JournaalPO_.boekingen))));

            previousJournaalid = entityManager.createQuery(criteriaQueryPreviousJournaalid).getSingleResult();
        }

        if (previousJournaalid == null) {
            Query patternQuery = entityManager.createNativeQuery(
                    "select max(id) from journaal J "
                            + " where J.transactie like ( "
                            + " select pattern from patterns "
                            + " where (select J2.transactie from journaal J2 where J2.id = ? ) like pattern ) "
                            + " and 1<=( select count(*) from boeking B where B.journaalid = J.id ) "
            );

            patternQuery.setParameter(1, nieuwJournaal.getId());
            List<BigInteger> resultList = patternQuery.getResultList();
            if (!resultList.isEmpty() && resultList.get(0) != null) {
                previousJournaalid = resultList.get(0).longValue();
            }
        }

        logger.info("tegenpartijRekening " + nieuwJournaal.getTegenpartijRekening() + " => " +
                previousJournaalid);

        CriteriaBuilder cb2 = entityManager.getCriteriaBuilder();

        CriteriaQuery<Boeking> criteriaQueryPreviousBoekingen = cb2.createQuery(Boeking.class);
        Root<BoekingPO> root = criteriaQueryPreviousBoekingen.from(BoekingPO.class);
        criteriaQueryPreviousBoekingen.select(root);
        criteriaQueryPreviousBoekingen.where(cb2.equal(root.get(BoekingPO_.journaal), previousJournaalid));

        return entityManager.createQuery(criteriaQueryPreviousBoekingen).getResultList();
    }

    private class CheckFirstlineFilter implements Predicate<String> {
        private boolean firstline = true;

        public boolean isFirstline() {
            return this.firstline;
        }

        @Override
        public boolean test(String t) {
            if (!firstline)
                return true;

            firstline = false;
            if (!t.equals("Rekening;Boekingsdatum;Rekeninguittrekselnummer;Transactienummer;Rekening tegenpartij;Naam tegenpartij bevat;Straat en nummer;"
                    + "Postcode en plaats;Transactie;Valutadatum;Bedrag;Devies;BIC;Landcode;Mededelingen"))
                throw new RuntimeException("Header lijn is niet zoals verwacht");

            return false;
        }
    }

    private class IntermediairJournaal {
        private String rekening;
        private LocalDate boekingsdatum;
        private Integer afschriftnummer;
        private Integer transactienummer;
        private String tegenpartijRekenening;
        private String tegenpartijNaam;
        private String tegenpartijAdres;
        private String tegenpartijPlaats;
        private String transactie;
        private LocalDate valutadatum;
        private BigDecimal bedrag;
        private String devies;
        private String bic;
        private String landcode;

        public IntermediairJournaal(String[] lijn) {
            int index = 0;
            this.rekening = lijn[index++];
            this.boekingsdatum = LocalDate.parse(lijn[index++], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            this.afschriftnummer = Integer.valueOf(StringUtils.defaultIfEmpty(lijn[index++], "0"));
            this.transactienummer = Integer.valueOf(StringUtils.defaultIfEmpty(lijn[index++], "0"));
            this.tegenpartijRekenening = lijn[index++];
            this.tegenpartijNaam = lijn[index++];
            this.tegenpartijAdres = lijn[index++];
            this.tegenpartijPlaats = lijn[index++];
            this.transactie = lijn[index++];
            this.valutadatum = LocalDate.parse(lijn[index++], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            this.bedrag = new BigDecimal(lijn[index++].replace(',', '.'));
            this.devies = lijn[index++];
            this.bic = lijn[index++];
            this.landcode = lijn[index++];
        }

        public String getRekening() {
            return rekening;
        }

        public LocalDate getBoekingsdatum() {
            return boekingsdatum;
        }

        public Integer getAfschriftnummer() {
            return afschriftnummer;
        }

        public Integer getTransactienummer() {
            return transactienummer;
        }

        public String getTegenpartijRekenening() {
            return tegenpartijRekenening;
        }

        public String getTegenpartijNaam() {
            return tegenpartijNaam;
        }

        public String getTegenpartijAdres() {
            return tegenpartijAdres;
        }

        public String getTegenpartijPlaats() {
            return tegenpartijPlaats;
        }

        public String getTransactie() {
            return transactie;
        }

        public LocalDate getValutadatum() {
            return valutadatum;
        }

        public BigDecimal getBedrag() {
            return bedrag;
        }

        public String getDevies() {
            return devies;
        }

        public String getBic() {
            return bic;
        }

        public String getLandcode() {
            return landcode;
        }

    }
}
