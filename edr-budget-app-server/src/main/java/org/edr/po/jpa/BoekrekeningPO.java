package org.edr.po.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.edr.po.Boekrekening;
import org.edr.util.jpa.JpaIdentifiableVersioned;

@Entity
@Table(name = "boekrekening")
public class BoekrekeningPO extends JpaIdentifiableVersioned implements Boekrekening {

	private String rekeningnr;
	private String omschrijving;
	private Boolean boekbaar;
	private Boolean budgeteerbaar;

	@Override
	@Column(name = "rekeningnr")
	public String getRekeningnr() {
		return rekeningnr;
	}

	@Override
	public void setRekeningnr(String rekeningnr) {
		this.rekeningnr = rekeningnr;
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
	@Column(name = "boekbaar")
	public Boolean getBoekbaar() {
		return boekbaar;
	}

	@Override
	public void setBoekbaar(Boolean boekbaar) {
		this.boekbaar = boekbaar;
	}

	@Override
	@Column(name = "budgeteerbaar")
	public Boolean getBudgeteerbaar() {
		return budgeteerbaar;
	}

	@Override
	public void setBudgeteerbaar(Boolean budgeteerbaar) {
		this.budgeteerbaar = budgeteerbaar;
	}

}
