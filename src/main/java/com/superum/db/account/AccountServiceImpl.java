package com.superum.db.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.db.account.roles.AccountRoles;
import com.superum.db.account.roles.AccountRolesDAO;

@Service
public class AccountServiceImpl implements AccountService {

	@Override
	public Account createNewAdmin(Account account) {
		Account newAdmin = accountDAO.create(account);
		accountRolesDAO.create(new AccountRoles(account.getUsername(), AccountType.COTEM.roleNames()));
		return newAdmin;
	}
	
	@Override
	public Account updateAccount(Account account) {
		return accountDAO.update(account);
	}

	@Override
	public Account retrieveInfo(String username) {
		return accountDAO.read(username);
	}
	
	// CONSTRUCTORS

	@Autowired
	public AccountServiceImpl(AccountDAO accountDAO, AccountRolesDAO accountRolesDAO) {
		this.accountDAO = accountDAO;
		this.accountRolesDAO = accountRolesDAO;
	}

	// PRIVATE
	
	private final AccountDAO accountDAO;
	private final AccountRolesDAO accountRolesDAO;

}
