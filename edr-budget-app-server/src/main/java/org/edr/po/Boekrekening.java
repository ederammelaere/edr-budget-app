package org.edr.po;

public interface Boekrekening extends IdentifiableVersioned {

	String getRekeningnr();

	void setRekeningnr(String rekeningnr);

	String getOmschrijving();

	void setOmschrijving(String omschrijving);

	Boolean getBoekbaar();

	void setBoekbaar(Boolean boekbaar);

	Boolean getBudgeteerbaar();

	void setBudgeteerbaar(Boolean budgeteerbaar);

}