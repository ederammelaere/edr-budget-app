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
import java.util.Optional;

import org.edr.po.Bankrekening;
import org.edr.po.Journaal;
import org.edr.po.jpa.JournaalPO;
import org.edr.services.JournaalService;
import org.edr.util.services.StandaardAbstractService;

public class StandaardJournaalService extends StandaardAbstractService implements JournaalService {

	@Override
	public void loadJournaalFromStream(BufferedReader reader) {
		// Evalueren van header lijn
		Optional<String> eersteLijn = reader.lines().skip(12).findFirst();
		if (!eersteLijn.isPresent()) {
			throw new RuntimeException("Er werd geen header lijn gevonden");
		}
		if (!eersteLijn.get().equals(
				"Rekening;Boekingsdatum;Afschriftnummer;Transactienummer;Rekening tegenpartij;Naam tegenpartij bevat;Straat en nummer;"
						+ "Postcode en plaats;Transactie;Valutadatum;Bedrag;Devies;BIC;Landcode")) {
			throw new RuntimeException("Header lijn is niet zoals verwacht");
		}

		// Ophalen van de rekeningen
		List<Bankrekening> rekeningenList = entityManager.createQuery("from BankrekeningPO", Bankrekening.class).getResultList();
		Map<String, Bankrekening> rekeningen = new HashMap<>();
		for (Bankrekening rekening : rekeningenList) {
			rekeningen.put(rekening.getRekeningnr(), rekening);
		}

		// Ophalen van maximum datum en afschriftnummer
		Date sqlMaxDatum = (Date) entityManager.createNativeQuery("select max(datum) from journaal").getSingleResult();
		Instant instant = Instant.ofEpochMilli(sqlMaxDatum.getTime());
		LocalDate maxDatum = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		Integer maxAfschriftnummer = (Integer) entityManager.createNativeQuery("select max(afschriftnummer) from journaal where datum = ?")
				.setParameter(1, sqlMaxDatum).getSingleResult();

		// Doorlopen van bestand
		reader.lines()
				.skip(13)
				.map(s -> s.split(";"))
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
						&& (s.getDatum().compareTo(maxDatum) > 0 || (s.getDatum().equals(maxDatum) && s.getAfschriftnummer() > maxAfschriftnummer)))
				.forEach(s -> {
					if (!s.getLandcode().equals("BE")) {
						throw new RuntimeException("Onverwachte landcode " + s.getLandcode());
					}
					if (!s.getDevies().equals("EUR")) {
						throw new RuntimeException("Onverwachte munt " + s.getDevies());
					}

					entityManager.persist(s);
				});
		;

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
			this.afschriftnummer = Integer.valueOf(lijn[index++]);
			this.transactienummer = Integer.valueOf(lijn[index++]);
			this.tegenpartijRekenening = lijn[index++];
			this.tegenpartijNaam = lijn[index++];
			this.tegenpartijAdres = lijn[index++];
			this.tegenpartijPlaats = lijn[index++];
			this.transactie = lijn[index++];
			this.valutadatum = LocalDate.parse(lijn[index++], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			this.bedrag = new BigDecimal(lijn[index++]);
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
