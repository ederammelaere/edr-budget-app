package org.edr.sandbox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.edr.po.Bankrekening;
import org.edr.po.jpa.BankrekeningPO;
import org.edr.services.JournaalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BudgetApp {

	private static final Logger logger = LoggerFactory.getLogger(BudgetApp.class);

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional
	public void createBankrekening() {
		entityManager.find(BankrekeningPO.class, 1l);
		Bankrekening bankRekening = new BankrekeningPO();
		bankRekening.setOmschrijving("zichtrekening");
		bankRekening.setRekeningnr("BE02 256 66");
		bankRekening.setSaldo(BigDecimal.ZERO);
		bankRekening.setStartSaldo(BigDecimal.ZERO);
		entityManager.persist(bankRekening);
	}

	public static void main(String[] args) {
		logger.info("TEST LOGGER");

		Properties p = new Properties(System.getProperties());
		p.put("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
		p.put("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "OFF");
		System.setProperties(p);

		ApplicationContext context = new ClassPathXmlApplicationContext("org/edr/spring/server-bundle.xml");
		JournaalService journaalService = (JournaalService) context.getBean("journaalService");
		journaalService.loadJournaalFromStream(openReader("org/edr/xxx.csv"));

		((ConfigurableApplicationContext) context).close();
	}

	private static BufferedReader openReader(String classpathResource) {
		return new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(classpathResource)));
	}

}
