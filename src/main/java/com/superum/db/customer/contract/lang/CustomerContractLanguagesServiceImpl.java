package com.superum.db.customer.contract.lang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerContractLanguagesServiceImpl implements CustomerContractLanguagesService {

	@Override
	public CustomerContractLanguages addLanguagesToCustomerContract(CustomerContractLanguages languages) {
		return languagesDAO.create(languages);
	}

	@Override
	public CustomerContractLanguages getLanguagesForCustomerContract(int customerId) {
		return languagesDAO.read(customerId);
	}

	@Override
	public CustomerContractLanguages updateLanguagesForCustomerContract(CustomerContractLanguages languages) {
		return languagesDAO.update(languages);
	}

	@Override
	public CustomerContractLanguages deleteLanguagesForCustomerContract(int customerId) {
		return languagesDAO.delete(customerId);
	}

	@Override
	public CustomerContractLanguages deleteLanguagesForCustomerContract(CustomerContractLanguages languages) {
		return languagesDAO.delete(languages);
	}
	
	// CONSTRUCTORS

	@Autowired
	public CustomerContractLanguagesServiceImpl(CustomerContractLanguagesDAO languagesDAO) {
		this.languagesDAO = languagesDAO;
	}

	// PRIVATE
	
	private final CustomerContractLanguagesDAO languagesDAO;

}
