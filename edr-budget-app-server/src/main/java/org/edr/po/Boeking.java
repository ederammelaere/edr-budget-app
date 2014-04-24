package org.edr.po;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.edr.util.jpa.IdentifiableVersioned;

public interface Boeking extends IdentifiableVersioned {

	Bankrekening getBankrekening();

	void setBankrekening(Bankrekening bankrekening);

	Boekrekening getBoekrekening();

	void setBoekrekening(Boekrekening boekrekening);

	String getOmschrijving();

	void setOmschrijving(String omschrijving);

	LocalDate getDatum();

	void setDatum(LocalDate date);

	BigDecimal getBedrag();

	void setBedrag(BigDecimal bedrag);

}