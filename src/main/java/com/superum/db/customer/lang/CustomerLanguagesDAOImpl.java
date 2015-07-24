package com.superum.db.customer.lang;

import com.superum.db.generated.timestar.tables.records.CustomerLangRecord;
import com.superum.exception.DatabaseException;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep3;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.CUSTOMER_LANG;

@Repository
@Transactional
public class CustomerLanguagesDAOImpl implements CustomerLanguagesDAO {

	@Override
	public CustomerLanguages create(CustomerLanguages languages, int partitionId) {
		Integer customerId = languages.getCustomerId();
		List<String> languageList = languages.getLanguages();
		
		InsertValuesStep3<CustomerLangRecord, Integer, Integer, String> step = sql.insertInto(CUSTOMER_LANG, CUSTOMER_LANG.PARTITION_ID, CUSTOMER_LANG.CUSTOMER_ID, CUSTOMER_LANG.LANGUAGE_LEVEL);
		for (String language : languageList)
			step = step.values(partitionId, customerId, language);

		try {
            step.execute();
        } catch (DataAccessException e) {
            throw new DatabaseException("Couldn't insert languages: " + languages + "; it's possible that" +
                    "the customer with this id doesn't exist, or languages for this customer" +
                    "have already been inserted; please refer to the nested exception for more info.", e);
        }

		return languages;
	}

	@Override
	public CustomerLanguages read(Integer customerId, int partitionId) {
        try {
            List<String> languages = sql.select(CUSTOMER_LANG.LANGUAGE_LEVEL)
                    .from(CUSTOMER_LANG)
                    .where(CUSTOMER_LANG.CUSTOMER_ID.eq(customerId)
                            .and(CUSTOMER_LANG.PARTITION_ID.eq(partitionId)))
                    .fetch()
                    .map(record -> record.getValue(CUSTOMER_LANG.LANGUAGE_LEVEL));
            return new CustomerLanguages(customerId, languages);
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read customer languages with id " + customerId, e);
        }
	}

	@Override
	public CustomerLanguages update(CustomerLanguages languages, int partitionId) {
        try {
            CustomerLanguages old = delete(languages.getCustomerId(), partitionId);

            create(languages, partitionId);
            return old;
        } catch (DatabaseException e) {
            throw new DatabaseException("An unexpected error occurred when trying to update customer languages " + languages, e);
        }
	}

	@Override
	public CustomerLanguages delete(Integer customerId, int partitionId) {
		return delete(new CustomerLanguages(customerId, Collections.emptyList()), partitionId);
	}

	@Override
	public CustomerLanguages delete(CustomerLanguages languages, int partitionId) {
        try {
            Integer customerId = languages.getCustomerId();

            CustomerLanguages old = read(customerId, partitionId);

            Condition condition = CUSTOMER_LANG.CUSTOMER_ID.eq(customerId)
                    .and(CUSTOMER_LANG.PARTITION_ID.eq(partitionId));
            for (String language : languages.getLanguages())
                condition = condition.and(CUSTOMER_LANG.LANGUAGE_LEVEL.eq(language));

            sql.delete(CUSTOMER_LANG)
                    .where(condition)
                    .execute();

            return old;
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to delete customer languages " + languages, e);
        }
	}
	
	// CONSTRUCTORS

	@Autowired
	public CustomerLanguagesDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
