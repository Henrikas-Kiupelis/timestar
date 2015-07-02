package com.superum.db.account;

import static com.superum.db.generated.timestar.Tables.ACCOUNT;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.exception.DatabaseException;

@Repository
@Transactional
public class AccountDAOImpl implements AccountDAO {

	@Override
	public Account create(Account account) {
		int id = account.getId();
		String username = account.getUsername();
		String accountType = account.getAccountType();
		String password = account.getPassword();
		
		int createResult = sql.insertInto(ACCOUNT)
				.set(ACCOUNT.ID, id)
				.set(ACCOUNT.USERNAME, username)
				.set(ACCOUNT.ACCOUNT_TYPE, accountType)
				.set(ACCOUNT.PASSWORD, password)
				.execute();
		if (createResult == 0)
			throw new DatabaseException("Couldn't insert contract: " + account);
		
		return new Account(id, username, accountType, password.toCharArray());
	}
	
	@Override
	public Account read(String username) {
		return sql.selectFrom(ACCOUNT)
				.where(ACCOUNT.USERNAME.eq(username))
				.fetch().stream()
				.findFirst()
				.map(Account::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find account with username: " + username));
	}

	@Override
	public Account update(Account account) {
		String username = account.getUsername();
		
		Account old = read(username);
		
		sql.update(ACCOUNT)
			.set(ACCOUNT.PASSWORD, account.getPassword())
			.where(ACCOUNT.USERNAME.eq(username))
			.execute();
	
		return old;
	}

	@Override
	public Account delete(String username) {
		Account old = read(username);
		
		int deleteResult = sql.delete(ACCOUNT)
				.where(ACCOUNT.USERNAME.eq(username))
				.execute();
		if (deleteResult == 0)
			throw new DatabaseException("Couldn't delete account with username: " + username);
		
		return old;
	}

	// CONSTRUCTORS

	@Autowired
	public AccountDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
