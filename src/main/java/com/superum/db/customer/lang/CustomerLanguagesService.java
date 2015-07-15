package com.superum.db.customer.lang;

import org.springframework.stereotype.Service;

@Service
public interface CustomerLanguagesService {

	CustomerLanguages addLanguagesToCustomerContract(CustomerLanguages languages, int partitionId);
	
	CustomerLanguages getLanguagesForCustomerContract(int customerId, int partitionId);
	
	CustomerLanguages updateLanguagesForCustomerContract(CustomerLanguages languages, int partitionId);
	
	CustomerLanguages deleteLanguagesForCustomerContract(int customerId, int partitionId);
	
	CustomerLanguages deleteLanguagesForCustomerContract(CustomerLanguages languages, int partitionId);

}
