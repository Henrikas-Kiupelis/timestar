package com.superum.db.account.roles;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface AccountRolesDAO extends SimpleDAO<AccountRoles, String> {

	AccountRoles delete(AccountRoles accountRoles);
	
}
