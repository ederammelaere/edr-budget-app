package org.edr.domain.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.edr.domain.BudgetStaat;
import org.edr.po.Boekrekening;
import org.edr.util.domain.RekeningStelselUtil;

public class BudgetStaatDO implements BudgetStaat {

	private Boekrekening boekrekening;
	private BigDecimal geboektBedrag;
	private BigDecimal referentieBedrag;
	private BigDecimal referentieJaarBedrag;

	private BudgetStaat parentBudgetStaat;
	private List<BudgetStaat> childBudgetStaten;

	public BudgetStaatDO(Boekrekening boekrekening, BudgetStaat parentBudgetStaat) {
		this.boekrekening = boekrekening;
		this.geboektBedrag = BigDecimal.ZERO;
        this.referentieBedrag = BigDecimal.ZERO;
        this.referentieJaarBedrag = BigDecimal.ZERO;

		this.parentBudgetStaat = parentBudgetStaat;
		this.childBudgetStaten = new ArrayList<>();

		if (this.parentBudgetStaat == null) {
			if (!this.boekrekening.getRekeningnr().equals("000000")) {
				throw new RuntimeException("parent mag niet null zijn");
			}
		} else {
			if (!parentBudgetStaat.isParent(this)) {
				throw new RuntimeException("Ongeldige toekenning van parent");
			}
			this.parentBudgetStaat.addChildBudgetStaat(this);
		}
	}

	@Override
	public Boekrekening getBoekrekening() {
		return boekrekening;
	}

	@Override
	public BigDecimal getGeboektBedrag() {
		return geboektBedrag;
	}

	@Override
	public BigDecimal getReferentieBedrag() {
		return referentieBedrag;
	}

	@Override
	public BigDecimal getReferentieJaarBedrag() {
		return referentieJaarBedrag;
	}

	@Override
	public void addReferentieBedrag(BigDecimal referentieBedrag) {
		this.referentieBedrag = this.referentieBedrag.add(referentieBedrag);

		if (this.parentBudgetStaat != null) {
			this.parentBudgetStaat.addReferentieBedrag(referentieBedrag);
		}
	}

	@Override
	public void addReferentieJaarBedrag(BigDecimal referentieJaarBedrag) {
		this.referentieJaarBedrag = this.referentieJaarBedrag.add(referentieJaarBedrag);

		if (this.parentBudgetStaat != null) {
			this.parentBudgetStaat.addReferentieJaarBedrag(referentieJaarBedrag);
		}
	}

	@Override
	public void addGeboektBedrag(BigDecimal geboektBedrag) {
		this.geboektBedrag = this.geboektBedrag.add(geboektBedrag);

		if (this.parentBudgetStaat != null) {
			this.parentBudgetStaat.addGeboektBedrag(geboektBedrag);
		}
	}

	@Override
	public BudgetStaat getParentBudgetStaat() {
		return parentBudgetStaat;
	}

	@Override
	public List<BudgetStaat> getChildBudgetStaten() {
		return childBudgetStaten;
	}

	@Override
	public void addChildBudgetStaat(BudgetStaat childBudgetStaat) {
		if (!childBudgetStaat.isChild(this)) {
			throw new RuntimeException("Ongeldige toekenning van child");
		}
		this.childBudgetStaten.add(childBudgetStaat);
	}

	@Override
	public boolean isChild(BudgetStaat budgetStaat) {
		String parentRekeningnr = budgetStaat.getBoekrekening().getRekeningnr();
		String childRekeningnr = this.boekrekening.getRekeningnr();

		if (childRekeningnr.equals(parentRekeningnr)) {
			if (!this.boekrekening.getId().equals(budgetStaat.getBoekrekening().getId())) {
				throw new RuntimeException("rekeningnummers identiek voor verschillende boekrekeningen");
			}
			return false;
		}

		return RekeningStelselUtil.isChild(childRekeningnr, parentRekeningnr);
	}

	@Override
	public boolean isParent(BudgetStaat budgetStaat) {
		return budgetStaat.isChild(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		BudgetStaatDO rhs = (BudgetStaatDO) obj;
		return new EqualsBuilder().append(boekrekening, rhs.boekrekening)
				.append(geboektBedrag, rhs.geboektBedrag)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(171717, 373737).append(boekrekening).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("boekrekening", boekrekening)
				.append("geboektBedrag", geboektBedrag).toString();
	}

}
