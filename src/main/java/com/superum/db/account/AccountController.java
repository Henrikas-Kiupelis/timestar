package com.superum.db.account;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.superum.utils.PrincipalUtils;

@RestController
@RequestMapping(value = "/timestar/api")
public class AccountController {

	@RequestMapping(value = "/account/admin/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Account createNewAdmin(Principal user, @RequestBody @Valid Account account) {
		String username = PrincipalUtils.makeName(account.getUsername(), PrincipalUtils.partitionId(user));
		
		String securePassword = encoder.encode(account.getPassword());
		account.erasePassword();
		
		return accountService.createNewAdmin(new Account(0, username, AccountType.ADMIN.name(), securePassword.toCharArray()));
	}
	
	@RequestMapping(value = "/account/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Account updateAccount(Principal user, @RequestBody @Valid Account account) throws IllegalArgumentException {
		String username = PrincipalUtils.makeName(account.getUsername(), PrincipalUtils.partitionId(user));
		if (!user.getName().equals(username))
			throw new IllegalArgumentException("Users can only update their own account!");
			
		String securePassword = encoder.encode(account.getPassword());
		account.erasePassword();
		
		return accountService.updateAccount(new Account(0, username, account.getAccountType(), securePassword.toCharArray()));
	}
	
	@RequestMapping(value = "/account/info", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Account retrieveInfo(Principal user, @RequestParam(value="username") String username) {
		username = PrincipalUtils.makeName(username, PrincipalUtils.partitionId(user));
		return accountService.retrieveInfo(username);
	}

	// CONSTRUCTORS

	@Autowired
	public AccountController(AccountService accountService, PasswordEncoder encoder) {
		this.accountService = accountService;
		this.encoder = encoder;
	}

	// PRIVATE
	
	private final AccountService accountService;
	private final PasswordEncoder encoder;
	
}
