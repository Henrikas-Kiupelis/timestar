package com.superum.db.customer.contract.lang;

import static com.superum.db.generated.timestar.Tables.CUSTOMER_CONTRACT_LANG;

import java.util.Collections;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.generated.timestar.tables.records.CustomerContractLangRecord;

@Repository
@Transactional
public class CustomerContractLanguagesDAOImpl implements CustomerContractLanguagesDAO {

	@Override
	public CustomerContractLanguages create(CustomerContractLanguages languages) {
		Integer customerId = languages.getCustomerId();
		List<String> languageList = languages.getLanguages();
		
		InsertValuesStep2<CustomerContractLangRecord, Integer, String> step = sql.insertInto(CUSTOMER_CONTRACT_LANG, CUSTOMER_CONTRACT_LANG.CUSTOMER_ID, CUSTOMER_CONTRACT_LANG.LANGUAGE_LEVEL);
		for (String language : languageList)
			step = step.values(customerId, language);
		
		step.execute();
		return languages;
	}

	@Override
	public CustomerContractLanguages read(Integer customerId) {
		List<String> languages =  sql.select(CUSTOMER_CONTRACT_LANG.LANGUAGE_LEVEL)
				.from(CUSTOMER_CONTRACT_LANG)
				.where(CUSTOMER_CONTRACT_LANG.CUSTOMER_ID.eq(customerId))
				.fetch()
				.map(record -> record.getValue(CUSTOMER_CONTRACT_LANG.LANGUAGE_LEVEL));
		return new CustomerContractLanguages(customerId, languages);
	}

	@Override
	public CustomerContractLanguages update(CustomerContractLanguages languages) {
		CustomerContractLanguages old = delete(languages.getCustomerId());
		
		create(languages);
		return old;
	}

	@Override
	public CustomerContractLanguages delete(Integer customerId) {
		return delete(new CustomerContractLanguages(customerId, Collections.emptyList()));
	}

	@Override
	public CustomerContractLanguages delete(CustomerContractLanguages languages) {
		Integer customerId = languages.getCustomerId();
		
		CustomerContractLanguages old = read(customerId);
		
		List<String> languageList = languages.getLanguages();
		
		Condition condition = CUSTOMER_CONTRACT_LANG.CUSTOMER_ID.eq(customerId);
		for (String language : languageList)
			condition = condition.and(CUSTOMER_CONTRACT_LANG.LANGUAGE_LEVEL.eq(language));
		
		sql.delete(CUSTOMER_CONTRACT_LANG)
			.where(condition)
			.execute();
		
		return old;
	}
	
	// CONSTRUCTORS

	@Autowired
	public CustomerContractLanguagesDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
