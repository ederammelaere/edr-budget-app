package org.edr.sandbox;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.edr.po.Bankrekening;
import org.edr.po.jpa.BankrekeningPO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BudgetApp {

	private EntityManager entityManager;
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
	public void createBankrekening()
	{
		entityManager.find(BankrekeningPO.class, 1l);
		Bankrekening bankRekening = new BankrekeningPO();
		bankRekening.setOmschrijving("zichtrekening");
		bankRekening.setRekeningnr("BE02 256 66");
		bankRekening.setSaldo(BigDecimal.ZERO);
		bankRekening.setStartSaldo(BigDecimal.ZERO);
		entityManager.persist(bankRekening);
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("org/edr/spring/application-context.xml");
		BudgetApp app = (BudgetApp) context.getBean("app");
		app.createBankrekening();		
		((ConfigurableApplicationContext)context).close();
	}

}
