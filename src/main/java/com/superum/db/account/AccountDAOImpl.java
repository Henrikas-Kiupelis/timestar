package com.superum.db.account;

import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.superum.db.generated.timestar.Tables.ACCOUNT;

@Repository
@Transactional
public class AccountDAOImpl implements AccountDAO {

	@Override
	public Account create(Account account) {
		try {
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
                throw new DatabaseException("Couldn't insert account: " + account);

            return new Account(id, username, accountType, password.toCharArray());
		} catch (DataAccessException e) {
            throw new DatabaseException("Couldn't insert account: " + account +
                    "; please refer to the nested exception for more info.", e);
        }
	}
	
	@Override
	public Account read(String username) {
        try {
            return sql.selectFrom(ACCOUNT)
                    .where(ACCOUNT.USERNAME.eq(username))
                    .fetch().stream()
                    .findFirst()
                    .map(Account::valueOf)
                    .orElseThrow(() -> new DatabaseException("Couldn't find account with username: " + username));
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read account for username " + username, e);
        }
	}

	@Override
	public Account update(Account account) {
        try {
            String username = account.getUsername();

            Account old = read(username);

            sql.update(ACCOUNT)
                    .set(ACCOUNT.PASSWORD, account.getPassword())
                    .where(ACCOUNT.USERNAME.eq(username))
                    .execute();

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to update account " + account, e);
        }
	}

	@Override
	public Account delete(String username) {
        try {
            Account old = read(username);

            int deleteResult = sql.delete(ACCOUNT)
                    .where(ACCOUNT.USERNAME.eq(username))
                    .execute();
            if (deleteResult == 0)
                throw new DatabaseException("Couldn't delete account with username: " + username);

            return old;
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete account for username " + username, e);
        }
	}

	// CONSTRUCTORS

	@Autowired
	public AccountDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
