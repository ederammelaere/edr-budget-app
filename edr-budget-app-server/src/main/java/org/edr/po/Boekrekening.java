package org.edr.po;

import org.edr.util.jpa.IdentifiableVersioned;

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