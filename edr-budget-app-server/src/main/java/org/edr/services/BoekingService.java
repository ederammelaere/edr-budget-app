package org.edr.services;

import java.time.LocalDate;
import java.util.List;

import org.edr.po.Boeking;
import org.edr.po.Journaal;

public interface BoekingService {

	void createBoeking(Boeking boeking);

	List<Boeking> findBoekingen(int jaar);

	void updateBoeking(Boeking boeking);

	void deleteBoeking(Long id);

	void saveBoekingen(Journaal journaal);

	List<Boeking> findManueleBoekingen(LocalDate referentiedatum);

}
