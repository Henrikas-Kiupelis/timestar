package com.superum.db.customer.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerLanguagesServiceImpl implements CustomerLanguagesService {

	@Override
	public CustomerLanguages addLanguagesToCustomerContract(CustomerLanguages languages, int partitionId) {
		LOG.debug("Creating new CustomerContractLanguages: {}", languages);
		
		CustomerLanguages newLanguages = languagesDAO.create(languages, partitionId);
		LOG.debug("New CustomerContractLanguages created: {}", newLanguages);
		
		return newLanguages;
	}

	@Override
	public CustomerLanguages getLanguagesForCustomerContract(int customerId, int partitionId) {
		LOG.debug("Reading CustomerContractLanguages for ID: {}", customerId);
		
		CustomerLanguages languages = languagesDAO.read(customerId, partitionId);
		LOG.debug("CustomerContractLanguages read: {}", languages);
		
		return languages;
	}

	@Override
	public CustomerLanguages updateLanguagesForCustomerContract(CustomerLanguages languages, int partitionId) {
		LOG.debug("Updating CustomerContractLanguages: {}", languages);
		
		CustomerLanguages oldLanguages = languagesDAO.update(languages, partitionId);
		LOG.debug("Old CustomerContractLanguages retrieved: {}", oldLanguages);
		
		return oldLanguages;
	}

	@Override
	public CustomerLanguages deleteLanguagesForCustomerContract(int customerId, int partitionId) {
		LOG.debug("Deleting CustomerContractLanguages for ID: {}", customerId);
		
		CustomerLanguages deletedLanguages = languagesDAO.delete(customerId, partitionId);
		LOG.debug("Deleted CustomerContractLanguages: {}", deletedLanguages);
		
		return deletedLanguages;
	}

	@Override
	public CustomerLanguages deleteLanguagesForCustomerContract(CustomerLanguages languages, int partitionId) {
		LOG.debug("Deleting CustomerContractLanguages: {}", languages);
		
		CustomerLanguages languagesBeforeDeletion = languagesDAO.delete(languages, partitionId);
		LOG.debug("CustomerContractLanguages before deletion: {}", languagesBeforeDeletion);
		
		return languagesBeforeDeletion;
	}
	
	// CONSTRUCTORS

	@Autowired
	public CustomerLanguagesServiceImpl(CustomerLanguagesDAO languagesDAO) {
		this.languagesDAO = languagesDAO;
	}

	// PRIVATE
	
	private final CustomerLanguagesDAO languagesDAO;
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomerLanguagesService.class);

}
