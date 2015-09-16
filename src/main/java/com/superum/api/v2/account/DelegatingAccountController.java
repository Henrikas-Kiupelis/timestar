package com.superum.api.v2.account;

import com.superum.api.v1.account.Account;
import com.superum.api.v1.account.AccountController;
import com.superum.api.v2.exception.InvalidRequestException;
import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * <pre>
 * API v2
 * Manages all requests for accounts
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 *
 * Delegates the work to API v1, because the methods are not changing
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/account")
public class DelegatingAccountController {

    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_UTF8, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidAccountDTO createAdmin(PartitionAccount partitionAccount, @RequestBody ValidAccountDTO validAccountDTO) {
        if (validAccountDTO == null)
            throw new InvalidRequestException("Account cannot be null");

        Account account = accountController.createNewAdmin(partitionAccount, validAccountDTO.toAccount());
        return ValidAccountDTO.valueOf(account);
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_UTF8, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidAccountDTO update(PartitionAccount partitionAccount, @RequestBody ValidAccountDTO validAccountDTO) {
        if (validAccountDTO == null)
            throw new InvalidRequestException("Account cannot be null");

        Account account = accountController.updateAccount(partitionAccount, validAccountDTO.toAccount());
        return ValidAccountDTO.valueOf(account);
    }

    @RequestMapping(method = GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidAccountDTO read(PartitionAccount partitionAccount, @RequestParam(value="username") String username) {
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
