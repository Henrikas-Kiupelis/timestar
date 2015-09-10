package com.superum.api.v1.account;

import org.springframework.stereotype.Service;

@Service
public interface AccountService {

	Account createNewAdmin(Account account);
	
	Account updateAccount(Account account);
	
	Account retrieveInfo(String username);

}
