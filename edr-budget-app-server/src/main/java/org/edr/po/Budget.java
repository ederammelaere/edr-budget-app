package org.edr.po;

import java.math.BigDecimal;

public interface Budget {

	Integer getJaar();

	void setJaar(Integer jaar);

	Boekrekening getBoekrekening();

	void setBoekrekening(Boekrekening boekrekening);

	BigDecimal getBedrag();

	void setBedrag(BigDecimal bedrag);

}