package org.edr.po;

import java.math.BigDecimal;

import org.edr.util.jpa.IdentifiableVersioned;

public interface Budget extends IdentifiableVersioned {

	Integer getJaar();

	void setJaar(Integer jaar);

	Boekrekening getBoekrekening();

	void setBoekrekening(Boekrekening boekrekening);

	BigDecimal getBedrag();

	void setBedrag(BigDecimal bedrag);

}