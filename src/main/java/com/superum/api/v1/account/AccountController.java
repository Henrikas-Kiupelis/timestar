package com.superum.api.v1.account;

import com.superum.api.exception.UnauthorizedRequestException;
import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/timestar/api")
public class AccountController {

	@RequestMapping(value = "/account/admin/add", method = POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Account createNewAdmin(PartitionAccount partitionAccount, @RequestBody @Valid Account account) {
		String username = partitionAccount.usernameFor(account);
		
		String securePassword = encoder.encode(account.getPassword());
		account.erasePassword();
		
		return accountService.createNewAdmin(new Account(0, username, AccountType.ADMIN.name(), securePassword.toCharArray()));
	}
	
	@RequestMapping(value = "/account/update", method = POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Account updateAccount(PartitionAccount partitionAccount, @RequestBody @Valid Account account) {
        if (!partitionAccount.belongsTo(account))
			throw new UnauthorizedRequestException("Users can only update their own account!");
			
		String securePassword = encoder.encode(account.getPassword());
		account.erasePassword();
		
		return accountService.updateAccount(new Account(0, partitionAccount.accountUsername(),
                account.getAccountType(),securePassword.toCharArray()));
	}
	
	@RequestMapping(value = "/account/info", method = GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Account retrieveInfo(PartitionAccount partitionAccount, @RequestParam(value="username") String username) {
		return accountService.retrieveInfo(partitionAccount.usernameFor(username));
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
