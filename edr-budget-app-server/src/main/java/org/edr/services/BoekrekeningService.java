package org.edr.services;

import java.util.List;

import org.edr.po.Boekrekening;

public interface BoekrekeningService {

	void createBoekrekening(Boekrekening boekrekening);

	List<Boekrekening> findBoekrekeningen();

	void updateBoekrekening(Boekrekening boekrekening);

	void deleteBoekrekening(Long id);

}
