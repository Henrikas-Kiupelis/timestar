package com.superum.db.account.roles;

import com.superum.db.generated.timestar.tables.records.RolesRecord;
import com.superum.exception.DatabaseException;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.ROLES;

@Repository
@Transactional
@Deprecated // Roles are automatically inserted in the database
public class AccountRolesDAOImpl implements AccountRolesDAO {

	@Override
	public AccountRoles create(AccountRoles roles) {
		try {
            String username = roles.getUsername();
            List<String> roleList = roles.getRoles();

            InsertValuesStep2<RolesRecord, String, String> step = sql.insertInto(ROLES, ROLES.USERNAME, ROLES.ROLE);
            for (String role : roleList)
                step = step.values(username, role);

            step.execute();
            return roles;
        } catch (DataAccessException e) {
            throw new DatabaseException("Couldn't insert account roles: " + roles + "; it's possible that " +
                    "the account with this username doesn't exist, or roles for this account " +
                    "have already been inserted; please refer to the nested exception for more info.", e);
        }
	}

	@Override
	public AccountRoles read(String username) {
        try {
            List<String> roles = sql.select(ROLES.ROLE)
                    .from(ROLES)
                    .where(ROLES.USERNAME.eq(username))
                    .fetch()
                    .map(record -> record.getValue(ROLES.ROLE));
            return new AccountRoles(username, roles);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read account roles for username " + username, e);
        }
	}

	@Override
	public AccountRoles update(AccountRoles roles) {
        try {
            AccountRoles old = delete(roles.getUsername());

            create(roles);
            return old;
        } catch (DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to update account roles " + roles, e);
        }
	}

	@Override
	public AccountRoles delete(String username) {
        try {
            return delete(new AccountRoles(username, Collections.emptyList()));
        } catch (DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete account roles for username " + username, e);
        }
	}

	@Override
	public AccountRoles delete(AccountRoles roles) {
        try {
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
        } catch (DataAccessException|DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete account roles " + roles, e);
        }
	}
	
	// CONSTRUCTORS

	@Autowired
	public AccountRolesDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
