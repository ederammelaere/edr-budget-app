package org.edr.services.impl;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.edr.po.Bankrekening;
import org.edr.po.Journaal;
import org.edr.po.jpa.JournaalPO;
import org.edr.po.jpa.JournaalPO_;
import org.edr.services.JournaalService;
import org.edr.util.services.StandaardAbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		Date sqlMaxDatum = (Date) entityManager.createNativeQuery("select max(datum) from journaal").getSingleResult();
		LocalDate maxDatum = sqlMaxDatum == null ? null : LocalDateTime.ofInstant(
				Instant.ofEpochMilli(sqlMaxDatum.getTime()), ZoneId.systemDefault()).toLocalDate();
		Integer maxAfschriftnummer = maxDatum == null ? null : (Integer) entityManager
				.createNativeQuery("select max(afschriftnummer) from journaal where datum = ?")
				.setParameter(1, sqlMaxDatum).getSingleResult();

		logger.info("maxDatum = " + maxDatum + "; maxAfschriftnummer = " + maxAfschriftnummer);

		// Evalueren van header lijn
		CheckFirstlineFilter firstlineFilter = new CheckFirstlineFilter();

		// Doorlopen van bestand
		reader.lines()
				.skip(12)
				.filter(firstlineFilter)
				.map(s -> s.split(";", -1))
				.map(s -> new IntermediairJournaal(s))
				.map(s -> {
					Journaal journaal = new JournaalPO();

					journaal.setBankrekening(rekeningen.get(s.getRekening()));
					journaal.setDatum(s.getBoekingsdatum());
					journaal.setAfschriftnummer(s.getAfschriftnummer());
					journaal.setTransactienummer(s.getTransactienummer());
					journaal.setTegenpartijRekening(s.getTegenpartijRekenening());
					journaal.setTegenpartijEigenRekening(rekeningen.get(s.getTegenpartijRekenening()));
					journaal.setTegenpartijNaam(s.getTegenpartijNaam());
					journaal.setTegenpartijAdres(s.getTegenpartijAdres());
					journaal.setTegenpartijPlaats(s.getTegenpartijPlaats());
					journaal.setTransactie(s.getTransactie());
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
					if (!s.getLandcode().equals("BE") && !s.getLandcode().isEmpty()) {
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
			if (!t.equals("Rekening;Boekingsdatum;Afschriftnummer;Transactienummer;Rekening tegenpartij;Naam tegenpartij bevat;Straat en nummer;"
					+ "Postcode en plaats;Transactie;Valutadatum;Bedrag;Devies;BIC;Landcode"))
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
