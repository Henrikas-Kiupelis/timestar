package com.superum.db.customer.contract.lang;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class CustomerContractLanguagesController {

	@RequestMapping(value = "/customer/contract/lang/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContractLanguages addLanguagesToCustomerContract(CustomerContractLanguages languages) {
		return languageService.addLanguagesToCustomerContract(languages);
	}
	
	@RequestMapping(value = "/customer/contract/lang/{customerId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContractLanguages getLanguagesForCustomerContract(int customerId) {
		return languageService.getLanguagesForCustomerContract(customerId);
	}
	
	@RequestMapping(value = "/customer/contract/lang/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContractLanguages updateLanguagesForCustomerContract(CustomerContractLanguages languages) {
		return languageService.updateLanguagesForCustomerContract(languages);
	}
	
	@RequestMapping(value = "/customer/contract/lang/delete/{customerId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContractLanguages deleteLanguagesForCustomerContract(int customerId) {
		return languageService.deleteLanguagesForCustomerContract(customerId);
	}
	
	@RequestMapping(value = "/customer/contract/lang/delete", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContractLanguages deleteLanguagesForCustomerContract(CustomerContractLanguages languages) {
		return languageService.deleteLanguagesForCustomerContract(languages);
	}
	
	// CONSTRUCTORS

	@Autowired
	public CustomerContractLanguagesController(CustomerContractLanguagesService languageService) {
		this.languageService = languageService;
	}

	// PRIVATE
	
	private final CustomerContractLanguagesService languageService;

}
