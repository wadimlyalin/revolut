package ru.vadimlyalin.revolut;

import ru.vadimlyalin.revolut.model.Account;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.Path;
import java.math.BigDecimal;

/**
 * @author Вадим Лялин
 */
@Singleton
@Path("transferDao")
public class TransferDao {
	private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("h2");

	public void transfer(long accountFromId, long accountToId, BigDecimal sum) {
		if(accountFromId == accountToId)
			throw new IllegalArgumentException("Accounts are same");

		if(sum == null)
			throw new IllegalArgumentException("Sum must not be null");

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		try {
			// get source internal account
			Account accountFrom = getAccount(accountFromId, entityManager);
			// check existence
			if(accountFrom == null)
				throw new IllegalArgumentException("Source account does not exist");
			// check if amount of money is enough
			if(accountFrom.getAmount().compareTo(sum) < 0)
				throw new IllegalArgumentException("Source account has no enough money");

			// get destination internal account
			Account accountTo = getAccount(accountToId, entityManager);
			// check existence
			if(accountTo == null)
				throw new IllegalArgumentException("Destination account does not exist");

			// change amount
			accountFrom.setAmount(accountFrom.getAmount().subtract(sum));
			accountTo.setAmount(accountTo.getAmount().add(sum));

			transaction.commit();
		} finally {
			if (transaction.isActive())
				transaction.rollback();

			entityManager.close();
		}
	}

	private Account getAccount(long id, EntityManager entityManager) {
		return entityManager.find(Account.class, id);
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}
}
