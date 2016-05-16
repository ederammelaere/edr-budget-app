package org.edr.po;

import org.edr.util.jpa.IdentifiableVersioned;

import javax.persistence.Column;

public interface Boekrekening extends IdentifiableVersioned {

	String getRekeningnr();

	void setRekeningnr(String rekeningnr);

	String getOmschrijving();

	void setOmschrijving(String omschrijving);

	Boolean getTotalisatie();

	void setTotalisatie(Boolean totalisatie);
}