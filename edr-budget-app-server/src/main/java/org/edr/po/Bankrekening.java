package org.edr.po;

import java.math.BigDecimal;

public interface Bankrekening extends IdentifiableVersioned {

	String getRekeningnr();
	void setRekeningnr(String rekeningnr);
	
	String getOmschrijving();
	void setOmschrijving(String omschrijving);
	
	BigDecimal getStartSaldo();
	void setStartSaldo(BigDecimal startSaldo);
	
	BigDecimal getSaldo();
	void setSaldo(BigDecimal saldo);
	
}
