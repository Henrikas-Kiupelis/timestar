package com.superum.db.customer.lang;

import static com.superum.db.generated.timestar.Tables.CUSTOMER_LANG;

import java.util.Collections;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.generated.timestar.tables.records.CustomerLangRecord;

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
		
		step.execute();
		return languages;
	}

	@Override
	public CustomerLanguages read(Integer customerId, int partitionId) {
		List<String> languages =  sql.select(CUSTOMER_LANG.LANGUAGE_LEVEL)
				.from(CUSTOMER_LANG)
				.where(CUSTOMER_LANG.CUSTOMER_ID.eq(customerId)
						.and(CUSTOMER_LANG.PARTITION_ID.eq(partitionId)))
				.fetch()
				.map(record -> record.getValue(CUSTOMER_LANG.LANGUAGE_LEVEL));
		return new CustomerLanguages(customerId, languages);
	}

	@Override
	public CustomerLanguages update(CustomerLanguages languages, int partitionId) {
		CustomerLanguages old = delete(languages.getCustomerId(), partitionId);
		
		create(languages, partitionId);
		return old;
	}

	@Override
	public CustomerLanguages delete(Integer customerId, int partitionId) {
		return delete(new CustomerLanguages(customerId, Collections.emptyList()), partitionId);
	}

	@Override
	public CustomerLanguages delete(CustomerLanguages languages, int partitionId) {
		Integer customerId = languages.getCustomerId();
		
		CustomerLanguages old = read(customerId, partitionId);
		
		List<String> languageList = languages.getLanguages();
		
		Condition condition = CUSTOMER_LANG.CUSTOMER_ID.eq(customerId)
				.and(CUSTOMER_LANG.PARTITION_ID.eq(partitionId));
		for (String language : languageList)
			condition = condition.and(CUSTOMER_LANG.LANGUAGE_LEVEL.eq(language));
		
		sql.delete(CUSTOMER_LANG)
			.where(condition)
			.execute();
		
		return old;
	}
	
	// CONSTRUCTORS

	@Autowired
	public CustomerLanguagesDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
