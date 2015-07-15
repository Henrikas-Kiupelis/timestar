package com.superum.db.customer.lang;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.superum.utils.PrincipalUtils;

@RestController
@RequestMapping(value = "/timestar/api")
public class CustomerLanguagesController {

	@RequestMapping(value = "/customer/lang/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerLanguages addLanguagesToCustomerContract(Principal user, @RequestBody @Valid CustomerLanguages languages) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.addLanguagesToCustomerContract(languages, partitionId);
	}
	
	@RequestMapping(value = "/customer/lang/{customerId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerLanguages getLanguagesForCustomerContract(Principal user, @PathVariable int customerId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.getLanguagesForCustomerContract(customerId, partitionId);
	}
	
	@RequestMapping(value = "/customer/lang/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerLanguages updateLanguagesForCustomerContract(Principal user, @RequestBody @Valid CustomerLanguages languages) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.updateLanguagesForCustomerContract(languages, partitionId);
	}
	
	@RequestMapping(value = "/customer/lang/delete/{customerId:[\\d]+}", method = RequestMethod.DELETE, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerLanguages deleteLanguagesForCustomerContract(Principal user, @PathVariable int customerId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.deleteLanguagesForCustomerContract(customerId, partitionId);
	}
	
	@RequestMapping(value = "/customer/lang/delete", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerLanguages deleteLanguagesForCustomerContract(Principal user, @RequestBody @Valid CustomerLanguages languages) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.deleteLanguagesForCustomerContract(languages, partitionId);
	}
	
	// CONSTRUCTORS

	@Autowired
	public CustomerLanguagesController(CustomerLanguagesService languageService) {
		this.languageService = languageService;
	}

	// PRIVATE
	
	private final CustomerLanguagesService languageService;

}
