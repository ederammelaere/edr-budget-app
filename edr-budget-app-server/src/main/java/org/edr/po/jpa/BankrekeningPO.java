package org.edr.po.jpa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.edr.po.Bankrekening;
import org.edr.util.jpa.JpaIdentifiableVersioned;

@Entity
@Table(name = "bankrekening")
public class BankrekeningPO extends JpaIdentifiableVersioned implements Bankrekening {

	private String rekeningnr;
	private String omschrijving;
	private BigDecimal startSaldo;
	private BigDecimal saldo;

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
	@Column(name = "startsaldo")
	public BigDecimal getStartSaldo() {
		return startSaldo;
	}

	@Override
	public void setStartSaldo(BigDecimal startSaldo) {
		this.startSaldo = startSaldo;
	}

	@Override
	@Column(name = "saldo")
	public BigDecimal getSaldo() {
		return saldo;
	}

	@Override
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("rekeningnr", rekeningnr)
				.append("omschrijving", omschrijving).append("startSaldo", startSaldo).append("saldo", saldo)
				.toString();
	}

}
