package com.superum.api.account;

import com.superum.api.exception.InvalidRequestException;
import com.superum.db.account.Account;
import com.superum.db.account.AccountController;
import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.superum.api.account.ValidAccount.usernameSizeLimit;
import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;
import static com.superum.helper.validation.Validator.validate;

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

    @RequestMapping(value = "/admin/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidAccount createNewAdmin(PartitionAccount partitionAccount, @RequestBody ValidAccount validAccount) {
        if (validAccount == null)
            throw new InvalidRequestException("Account cannot be null");

        Account account = accountController.createNewAdmin(partitionAccount, validAccount.toAccount());
        return ValidAccount.valueOf(account);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidAccount updateAccount(PartitionAccount partitionAccount, @RequestBody ValidAccount validAccount) {
        if (validAccount == null)
            throw new InvalidRequestException("Account cannot be null");

        Account account = accountController.updateAccount(partitionAccount, validAccount.toAccount());
        return ValidAccount.valueOf(account);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidAccount retrieveInfo(PartitionAccount partitionAccount, @RequestParam(value="username") String username) {
        validate(username).not().Null().not().blank().fits(usernameSizeLimit())
                .ifInvalid(() -> new InvalidRequestException("Account username must not be null, blank or exceed " + usernameSizeLimit() + " chars: " + username));

        Account account = accountController.retrieveInfo(partitionAccount, username);
        return ValidAccount.valueOf(account);
    }

    // CONSTRUCTORS

    @Autowired
    public DelegatingAccountController(AccountController accountController) {
        this.accountController = accountController;
    }

    // PRIVATE

    private final AccountController accountController;

}
