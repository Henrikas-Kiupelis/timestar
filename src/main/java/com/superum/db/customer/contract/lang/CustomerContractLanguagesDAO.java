package com.superum.db.customer.contract.lang;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface CustomerContractLanguagesDAO extends SimpleDAO<CustomerContractLanguages, Integer> {

	CustomerContractLanguages delete(CustomerContractLanguages languages);
	
}
