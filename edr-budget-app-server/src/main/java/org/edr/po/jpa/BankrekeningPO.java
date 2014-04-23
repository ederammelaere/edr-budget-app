package org.edr.po.jpa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.edr.po.Bankrekening;

@Entity
@Table(name="bankrekening")
public class BankrekeningPO extends JpaIdentifiableVersioned implements Bankrekening {

	private String rekeningnr;
	private String omschrijving;
	private BigDecimal startSaldo;
	private BigDecimal saldo;
	
	@Column(name="rekeningnr")
	public String getRekeningnr() {
		return rekeningnr;
	}
	
	public void setRekeningnr(String rekeningnr) {
		this.rekeningnr = rekeningnr;
	}
	
	@Column(name="omschrijving")
	public String getOmschrijving() {
		return omschrijving;
	}
	
	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}
	
	@Column(name="startsaldo")
	public BigDecimal getStartSaldo() {
		return startSaldo;
	}
	
	public void setStartSaldo(BigDecimal startSaldo) {
		this.startSaldo = startSaldo;
	}
	
	@Column(name="saldo")
	public BigDecimal getSaldo() {
		return saldo;
	}
	
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

}
