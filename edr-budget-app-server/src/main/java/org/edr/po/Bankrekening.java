package org.edr.po;

import org.edr.util.jpa.IdentifiableVersioned;

public interface Bankrekening extends IdentifiableVersioned {

	String getRekeningnr();

	void setRekeningnr(String rekeningnr);

	String getOmschrijving();

	void setOmschrijving(String omschrijving);

}
