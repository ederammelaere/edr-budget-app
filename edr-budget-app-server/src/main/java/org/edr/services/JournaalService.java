package org.edr.services;

import java.io.BufferedReader;
import java.util.List;

import org.edr.po.Journaal;
import org.edr.po.jpa.BoekingPO;

public interface JournaalService {

	void loadJournaalFromStream(BufferedReader reader);

	List<Journaal> findJournaal(int jaar);

	List<BoekingPO> findPreviousBoekingen(String tegenpartijRekening);

}
