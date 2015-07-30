package com.superum.db.account.roles;

import org.springframework.stereotype.Repository;

@Repository
@Deprecated // Roles are automatically inserted in the database
public interface AccountRolesDAO {

	AccountRoles create(AccountRoles roles);

	AccountRoles read(String username);

	AccountRoles update(AccountRoles roles);

	AccountRoles delete(String username);
	
	AccountRoles delete(AccountRoles accountRoles);
	
}
