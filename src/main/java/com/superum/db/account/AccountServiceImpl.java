package com.superum.db.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Override
	public Account createNewAdmin(Account account) {
		LOG.debug("Creating new admin Account: {}", account);
		
		Account newAdmin = accountDAO.create(account);
		LOG.debug("New admin Account created: {}", newAdmin);

		return newAdmin;
	}
	
	@Override
	public Account updateAccount(Account account) {
		LOG.debug("Updating Account: {}", account);
		
		Account oldAccount = accountDAO.update(account);
		LOG.debug("Old Account retrieved: {}", oldAccount);
		
		return oldAccount;
	}

	@Override
	public Account retrieveInfo(String username) {
		LOG.debug("Reading Account info for username: {}", username);
		
		Account account = accountDAO.read(username);
		LOG.debug("Account retrieved: {}", account);
		
		return account;
	}
	
	// CONSTRUCTORS

	@Autowired
	public AccountServiceImpl(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	// PRIVATE
	
	private final AccountDAO accountDAO;
	
	private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

}
