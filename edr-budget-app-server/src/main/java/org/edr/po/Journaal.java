package org.edr.po;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.edr.util.jpa.IdentifiableVersioned;

public interface Journaal extends IdentifiableVersioned {

	Bankrekening getBankrekening();

	void setBankrekening(Bankrekening bankrekening);

	LocalDate getDatum();

	void setDatum(LocalDate datum);

	Integer getAfschriftnummer();

	void setAfschriftnummer(Integer afschriftnummer);

	Integer getTransactienummer();

	void setTransactienummer(Integer transactienummer);

	String getTegenpartijRekening();

	void setTegenpartijRekening(String tegenpartijRekening);

	Bankrekening getTegenpartijEigenRekening();

	void setTegenpartijEigenRekening(Bankrekening tegenpartijEigenRekening);

	String getTegenpartijNaam();

	void setTegenpartijNaam(String tegenpartijNaam);

	String getTegenpartijAdres();

	void setTegenpartijAdres(String tegenpartijAdres);

	String getTegenpartijPlaats();

	void setTegenpartijPlaats(String tegenpartijPlaats);

	String getTransactie();

	void setTransactie(String transactie);

	LocalDate getValutadatum();

	void setValutadatum(LocalDate valutadatum);

	BigDecimal getBedrag();

	void setBedrag(BigDecimal bedrag);

	String getDevies();

	void setDevies(String devies);

	String getBic();

	void setBic(String bic);

	String getLandcode();

	void setLandcode(String landcode);

}