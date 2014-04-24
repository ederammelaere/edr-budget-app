package org.edr.junit;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(TestConstants.SPRING_BUNDLE)
@Transactional
@TransactionConfiguration
public abstract class AbstractJunitTest {

	private static final Logger logger = LoggerFactory.getLogger(AbstractJunitTest.class);

	private static boolean initWithDbunitDone = false;
	private static IDataSet expectedDataSet = null;

	@Autowired
	private DataSource budgetDataSource;

	@PersistenceContext
	protected EntityManager entityManager;

	@BeforeTransaction
	public void initWithDbunit() throws SQLException, DatabaseUnitException {
		if (!initWithDbunitDone) {
			logger.info("Doe initialisatie testdata met dbunit...");

			IDatabaseConnection conn = new DatabaseDataSourceConnection(budgetDataSource);
			IDataSet dataSet = new FlatXmlDataSetBuilder().build(AbstractJunitTest.class.getResource("/org/edr/dbunit/init-data.xml"));
			DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
			conn.close();
			initWithDbunitDone = true;
		}
	}

	@BeforeClass
	public static void fillExpectedDataSet() throws DataSetException {
		logger.info("Opvullen dataset van dbunit met gewenste data...");
		expectedDataSet = new FlatXmlDataSetBuilder().build(AbstractJunitTest.class.getResource("/org/edr/dbunit/eval-data.xml"));
	}

	public void assertSQL(String tableName, String sql) {
		((Session) entityManager.getDelegate()).doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				// We moeten de controle doen binnen de huidige connectie - de
				// test is immers enkel zinvol binnen de lopende transactie
				try {
					IDatabaseConnection conn = new DatabaseConnection(connection);
					ITable actualTable = conn.createQueryTable(tableName, sql);
					ITable expectedTable = expectedDataSet.getTable(tableName);
					Assertion.assertEquals(expectedTable, actualTable);
				} catch (DatabaseUnitException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
}
