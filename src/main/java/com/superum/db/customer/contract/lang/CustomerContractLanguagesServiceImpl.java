package com.superum.db.customer.contract.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerContractLanguagesServiceImpl implements CustomerContractLanguagesService {

	@Override
	public CustomerContractLanguages addLanguagesToCustomerContract(CustomerContractLanguages languages) {
		LOG.debug("Creating new CustomerContractLanguages: {}", languages);
		
		CustomerContractLanguages newLanguages = languagesDAO.create(languages);
		LOG.debug("New CustomerContractLanguages created: {}", newLanguages);
		
		return newLanguages;
	}

	@Override
	public CustomerContractLanguages getLanguagesForCustomerContract(int customerId) {
		LOG.debug("Reading CustomerContractLanguages for ID: {}", customerId);
		
		CustomerContractLanguages languages = languagesDAO.read(customerId);
		LOG.debug("CustomerContractLanguages read: {}", languages);
		
		return languages;
	}

	@Override
	public CustomerContractLanguages updateLanguagesForCustomerContract(CustomerContractLanguages languages) {
		LOG.debug("Updating CustomerContractLanguages: {}", languages);
		
		CustomerContractLanguages oldLanguages = languagesDAO.update(languages);
		LOG.debug("Old CustomerContractLanguages retrieved: {}", oldLanguages);
		
		return oldLanguages;
	}

	@Override
	public CustomerContractLanguages deleteLanguagesForCustomerContract(int customerId) {
		LOG.debug("Deleting CustomerContractLanguages for ID: {}", customerId);
		
		CustomerContractLanguages deletedLanguages = languagesDAO.delete(customerId);
		LOG.debug("Deleted CustomerContractLanguages: {}", deletedLanguages);
		
		return deletedLanguages;
	}

	@Override
	public CustomerContractLanguages deleteLanguagesForCustomerContract(CustomerContractLanguages languages) {
		LOG.debug("Deleting CustomerContractLanguages: {}", languages);
		
		CustomerContractLanguages deletedLanguages = languagesDAO.delete(languages);
		LOG.debug("CustomerContractLanguages before deletion: {}", deletedLanguages);
		
		return deletedLanguages;
	}
	
	// CONSTRUCTORS

	@Autowired
	public CustomerContractLanguagesServiceImpl(CustomerContractLanguagesDAO languagesDAO) {
		this.languagesDAO = languagesDAO;
	}

	// PRIVATE
	
	private final CustomerContractLanguagesDAO languagesDAO;
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomerContractLanguagesService.class);

}
