package com.superum.db.customer.lang;

import com.superum.utils.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.superum.utils.ControllerUtils.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class CustomerLanguagesController {

	@RequestMapping(value = "/customer/lang/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public CustomerLanguages addLanguagesToCustomerContract(Principal user, @RequestBody @Valid CustomerLanguages languages) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.addLanguagesToCustomerContract(languages, partitionId);
	}
	
	@RequestMapping(value = "/customer/lang/{customerId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public CustomerLanguages getLanguagesForCustomerContract(Principal user, @PathVariable int customerId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.getLanguagesForCustomerContract(customerId, partitionId);
	}
	
	@RequestMapping(value = "/customer/lang/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public CustomerLanguages updateLanguagesForCustomerContract(Principal user, @RequestBody @Valid CustomerLanguages languages) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.updateLanguagesForCustomerContract(languages, partitionId);
	}
	
	@RequestMapping(value = "/customer/lang/delete/{customerId:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public CustomerLanguages deleteLanguagesForCustomerContract(Principal user, @PathVariable int customerId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return languageService.deleteLanguagesForCustomerContract(customerId, partitionId);
	}
	
	@RequestMapping(value = "/customer/lang/delete", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
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
