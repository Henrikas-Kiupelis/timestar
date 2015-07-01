package com.superum.db.customer.contract.lang;

import org.springframework.stereotype.Service;

@Service
public interface CustomerContractLanguagesService {

	CustomerContractLanguages addLanguagesToCustomerContract(CustomerContractLanguages languages);
	
	CustomerContractLanguages getLanguagesForCustomerContract(int customerId);
	
	CustomerContractLanguages updateLanguagesForCustomerContract(CustomerContractLanguages languages);
	
	CustomerContractLanguages deleteLanguagesForCustomerContract(int customerId);
	
	CustomerContractLanguages deleteLanguagesForCustomerContract(CustomerContractLanguages languages);

}
