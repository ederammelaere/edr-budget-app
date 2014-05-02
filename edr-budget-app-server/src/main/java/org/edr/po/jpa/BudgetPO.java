package org.edr.po.jpa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.edr.po.Boekrekening;
import org.edr.po.Budget;
import org.edr.util.jpa.JpaIdentifiableVersioned;

@Entity
@Table(name = "budget")
public class BudgetPO extends JpaIdentifiableVersioned implements Budget {

	private Integer jaar;
	private Boekrekening boekrekening;
	private BigDecimal bedrag;

	@Override
	@Column(name = "jaar")
	public Integer getJaar() {
		return jaar;
	}

	@Override
	public void setJaar(Integer jaar) {
		this.jaar = jaar;
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
	@Column(name = "bedrag")
	public BigDecimal getBedrag() {
		return bedrag;
	}

	@Override
	public void setBedrag(BigDecimal bedrag) {
		this.bedrag = bedrag;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("jaar", jaar)
				.append("boekrekening", boekrekening).append("bedrag", bedrag).toString();
	}

}
