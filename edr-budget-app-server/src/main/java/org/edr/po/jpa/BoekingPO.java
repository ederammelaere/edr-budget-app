package org.edr.po.jpa;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.edr.po.Bankrekening;
import org.edr.po.Boeking;
import org.edr.po.Boekrekening;
import org.edr.po.Journaal;
import org.edr.util.jpa.JpaIdentifiableVersioned;

@Entity
@Table(name = "boeking")
public class BoekingPO extends JpaIdentifiableVersioned implements Boeking {

	private Bankrekening bankrekening;
	private Boekrekening boekrekening;
	private String omschrijving;
	private LocalDate datum;
	private BigDecimal bedrag;
	private Journaal journaal;

	public BoekingPO() {
	}

	public BoekingPO(Bankrekening bankrekening, BigDecimal bedrag) {
		this.bankrekening = bankrekening;
		this.bedrag = bedrag;
	}

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
	@ManyToOne(targetEntity = BoekrekeningPO.class)
	@JoinColumn(name = "boekrekeningid")
	public Boekrekening getBoekrekening() {
		return boekrekening;
	}

	@Override
	public void setBoekrekening(Boekrekening boekrekening) {
		this.boekrekening = boekrekening;
	}

	@Override
	@Column(name = "omschrijving")
	public String getOmschrijving() {
		return omschrijving;
	}

	@Override
	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
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
	@Column(name = "bedrag")
	public BigDecimal getBedrag() {
		return bedrag;
	}

	@Override
	public void setBedrag(BigDecimal bedrag) {
		this.bedrag = bedrag;
	}

	@Override
	@ManyToOne(targetEntity = JournaalPO.class)
	@JoinColumn(name = "journaalid")
	public Journaal getJournaal() {
		return journaal;
	}

	@Override
	public void setJournaal(Journaal journaal) {
		this.journaal = journaal;
	}

	@Override
	@Transient
	public boolean getIsJournaalSet() {
		return this.journaal != null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("bankrekening", bankrekening)
				.append("boekrekening", boekrekening).append("omschrijving", omschrijving).append("datum", datum)
				.append("bedrag", bedrag).append("journaal", journaal).toString();
	}

}
