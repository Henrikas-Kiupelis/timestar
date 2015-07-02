package com.superum.db.customer.contract.lang;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class CustomerContractLanguagesController {

	@RequestMapping(value = "/customer/contract/lang/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContractLanguages addLanguagesToCustomerContract(@RequestBody @Valid CustomerContractLanguages languages) {
		return languageService.addLanguagesToCustomerContract(languages);
	}
	
	@RequestMapping(value = "/customer/contract/lang/{customerId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContractLanguages getLanguagesForCustomerContract(@PathVariable int customerId) {
		return languageService.getLanguagesForCustomerContract(customerId);
	}
	
	@RequestMapping(value = "/customer/contract/lang/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContractLanguages updateLanguagesForCustomerContract(@RequestBody @Valid CustomerContractLanguages languages) {
		return languageService.updateLanguagesForCustomerContract(languages);
	}
	
	@RequestMapping(value = "/customer/contract/lang/delete/{customerId:[\\d]+}", method = RequestMethod.DELETE, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContractLanguages deleteLanguagesForCustomerContract(@PathVariable int customerId) {
		return languageService.deleteLanguagesForCustomerContract(customerId);
	}
	
	@RequestMapping(value = "/customer/contract/lang/delete", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContractLanguages deleteLanguagesForCustomerContract(@RequestBody @Valid CustomerContractLanguages languages) {
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
