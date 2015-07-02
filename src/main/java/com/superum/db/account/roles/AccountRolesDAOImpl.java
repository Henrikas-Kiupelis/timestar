package com.superum.db.account.roles;

import static com.superum.db.generated.timestar.Tables.ROLES;

import java.util.Collections;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.generated.timestar.tables.records.RolesRecord;

@Repository
@Transactional
public class AccountRolesDAOImpl implements AccountRolesDAO {

	@Override
	public AccountRoles create(AccountRoles roles) {
		String username = roles.getUsername();
		List<String> roleList = roles.getRoles();
		
		InsertValuesStep2<RolesRecord, String, String> step = sql.insertInto(ROLES, ROLES.USERNAME, ROLES.ROLE);
		for (String role : roleList)
			step = step.values(username, role);
		
		step.execute();
		return roles;
	}

	@Override
	public AccountRoles read(String username) {
		List<String> roles = sql.select(ROLES.ROLE)
				.from(ROLES)
				.where(ROLES.USERNAME.eq(username))
				.fetch()
				.map(record -> record.getValue(ROLES.ROLE));
		return new AccountRoles(username, roles);
	}

	@Override
	public AccountRoles update(AccountRoles roles) {
		AccountRoles old = delete(roles.getUsername());
		
		create(roles);
		return old;
	}

	@Override
	public AccountRoles delete(String username) {
		return delete(new AccountRoles(username, Collections.emptyList()));
	}

	@Override
	public AccountRoles delete(AccountRoles roles) {
		String username = roles.getUsername();
		
		AccountRoles old = read(username);
		
		List<String> roleList = roles.getRoles();
		
		Condition condition = ROLES.USERNAME.eq(username);
		for (String role : roleList)
			condition = condition.and(ROLES.ROLE.eq(role));
		
		sql.delete(ROLES)
			.where(condition)
			.execute();
		
		return old;
	}
	
	// CONSTRUCTORS

	@Autowired
	public AccountRolesDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
