package org.edr.services;

import org.edr.po.Boeking;
import org.edr.po.Journaal;

import java.io.BufferedReader;
import java.util.List;

public interface JournaalService {

	void loadJournaalFromStream(BufferedReader reader);

	List<Journaal> findJournaal(int jaar);

	List<Boeking> findPreviousBoekingen(String tegenpartijRekening);

}
