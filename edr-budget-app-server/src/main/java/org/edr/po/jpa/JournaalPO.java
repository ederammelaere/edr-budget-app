package org.edr.po.jpa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.edr.po.Bankrekening;
import org.edr.po.Boeking;
import org.edr.po.Journaal;
import org.edr.util.jpa.JpaIdentifiableVersioned;

@Entity
@Table(name = "journaal")
public class JournaalPO extends JpaIdentifiableVersioned implements Journaal {

	private Bankrekening bankrekening;
	private LocalDate datum;
	private Integer afschriftnummer;
	private Integer transactienummer;
	private String tegenpartijRekening;
	private Bankrekening tegenpartijEigenRekening;
	private String tegenpartijNaam;
	private String tegenpartijAdres;
	private String tegenpartijPlaats;
	private String transactie;
	private LocalDate valutadatum;
	private BigDecimal bedrag;
	private String devies;
	private String bic;
	private String landcode;

	private Set<Boeking> boekingen;

	@Override
	@ManyToOne(targetEntity = BankrekeningPO.class)
	@JoinColumn(name = "bankrekeningid")
	public Bankrekening getBankrekening() {
		return bankrekening;
	}

	@Override
	public void setBankrekening(Bankrekening bankrekening) {
		this.bankrekening = bankrekening;
	}

	@Override
	@Column(name = "datum")
	public LocalDate getDatum() {
		return datum;
	}

	@Override
	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

	@Override
	@Column(name = "afschriftnummer")
	public Integer getAfschriftnummer() {
		return afschriftnummer;
	}

	@Override
	public void setAfschriftnummer(Integer afschriftnummer) {
		this.afschriftnummer = afschriftnummer;
	}

	@Override
	@Column(name = "transactienummer")
	public Integer getTransactienummer() {
		return transactienummer;
	}

	@Override
	public void setTransactienummer(Integer transactienummer) {
		this.transactienummer = transactienummer;
	}

	@Override
	@Column(name = "tegenpartij_rekening")
	public String getTegenpartijRekening() {
		return tegenpartijRekening;
	}

	@Override
	public void setTegenpartijRekening(String tegenpartijRekening) {
		this.tegenpartijRekening = tegenpartijRekening;
	}

	@Override
	@ManyToOne(targetEntity = BankrekeningPO.class)
	@JoinColumn(name = "tegenpartij_rekeningid")
	public Bankrekening getTegenpartijEigenRekening() {
		return tegenpartijEigenRekening;
	}

	@Override
	public void setTegenpartijEigenRekening(Bankrekening tegenpartijEigenRekening) {
		this.tegenpartijEigenRekening = tegenpartijEigenRekening;
	}

	@Override
	@Column(name = "tegenpartij_naam")
	public String getTegenpartijNaam() {
		return tegenpartijNaam;
	}

	@Override
	public void setTegenpartijNaam(String tegenpartijNaam) {
		this.tegenpartijNaam = tegenpartijNaam;
	}

	@Override
	@Column(name = "tegenpartij_adres")
	public String getTegenpartijAdres() {
		return tegenpartijAdres;
	}

	@Override
	public void setTegenpartijAdres(String tegenpartijAdres) {
		this.tegenpartijAdres = tegenpartijAdres;
	}

	@Override
	@Column(name = "tegenpartij_plaats")
	public String getTegenpartijPlaats() {
		return tegenpartijPlaats;
	}

	@Override
	public void setTegenpartijPlaats(String tegenpartijPlaats) {
		this.tegenpartijPlaats = tegenpartijPlaats;
	}

	@Override
	@Column(name = "transactie")
	public String getTransactie() {
		return transactie;
	}

	@Override
	public void setTransactie(String transactie) {
		this.transactie = transactie;
	}

	@Override
	@Column(name = "valutadatum")
	public LocalDate getValutadatum() {
		return valutadatum;
	}

	@Override
	public void setValutadatum(LocalDate valutadatum) {
		this.valutadatum = valutadatum;
	}

	@Override
	@Column(name = "bedrag")
	public BigDecimal getBedrag() {
		return bedrag;
	}

	@Override
	public void setBedrag(BigDecimal bedrag) {
		this.bedrag = bedrag;
	}

	@Override
	@Column(name = "devies")
	public String getDevies() {
		return devies;
	}

	@Override
	public void setDevies(String devies) {
		this.devies = devies;
	}

	@Override
	@Column(name = "bic")
	public String getBic() {
		return bic;
	}

	@Override
	public void setBic(String bic) {
		this.bic = bic;
	}

	@Override
	@Column(name = "landcode")
	public String getLandcode() {
		return landcode;
	}

	@Override
	public void setLandcode(String landcode) {
		this.landcode = landcode;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("bankrekening", bankrekening)
				.append("afschriftnummer", afschriftnummer).append("transactienummer", transactienummer)
				.append("datum", datum).append("transactie", transactie).toString();
	}

	@Override
	@OneToMany(mappedBy = "journaal", targetEntity = BoekingPO.class)
	public Set<Boeking> getBoekingen() {
		return boekingen;
	}

	@Override
	public void setBoekingen(Set<Boeking> boekingen) {
		this.boekingen = boekingen;
	}

}
