package com.superum.api.v1.account;

import org.springframework.stereotype.Repository;

@Repository
public interface AccountDAO {

	Account create(Account account);

	Account read(String username);

	Account update(Account account);

	Account delete(String username);
		
}
