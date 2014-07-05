package org.edr.services;

import java.util.List;

import org.edr.po.Bankrekening;

public interface BankrekeningService {

	void createBankrekening(Bankrekening bankrekening);

	List<Bankrekening> findBankrekeningen();

	void updateBankrekening(Bankrekening bankrekening);

	void deleteBankrekening(Long id);

}
