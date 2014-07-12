package org.edr.services;

import java.util.List;

import org.edr.po.Boeking;

public interface BoekingService {

	void createBoeking(Boeking boeking);

	List<Boeking> findBoekingen(int jaar);

	void updateBoeking(Boeking boeking);

	void deleteBoeking(Long id);

}
