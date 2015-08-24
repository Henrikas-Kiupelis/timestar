package com.superum.api.account;

import com.superum.api.exception.InvalidRequestException;
import com.superum.db.account.Account;
import com.superum.db.account.AccountController;
import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

/**
 * <pre>
 * API v2
 * Manages all requests for Accounts
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 *
 * Delegates the work to API v1, because the methods are not changing
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/account")
public class DelegatingAccountController {

    @RequestMapping(method = RequestMethod.PUT, consumes = APPLICATION_JSON_UTF8, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidAccountDTO createNewAdmin(PartitionAccount partitionAccount, @RequestBody ValidAccountDTO validAccountDTO) {
        if (validAccountDTO == null)
            throw new InvalidRequestException("Account cannot be null");

        Account account = accountController.createNewAdmin(partitionAccount, validAccountDTO.toAccount());
        return ValidAccountDTO.valueOf(account);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_UTF8, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidAccountDTO updateAccount(PartitionAccount partitionAccount, @RequestBody ValidAccountDTO validAccountDTO) {
        if (validAccountDTO == null)
            throw new InvalidRequestException("Account cannot be null");

        Account account = accountController.updateAccount(partitionAccount, validAccountDTO.toAccount());
        return ValidAccountDTO.valueOf(account);
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidAccountDTO retrieveInfo(PartitionAccount partitionAccount, @RequestParam(value="username") String username) {
        ValidAccountDTO.validateUsername(username);

        Account account = accountController.retrieveInfo(partitionAccount, username);
        return ValidAccountDTO.valueOf(account);
    }

    // CONSTRUCTORS

    @Autowired
    public DelegatingAccountController(AccountController accountController) {
        this.accountController = accountController;
    }

    // PRIVATE

    private final AccountController accountController;

}
