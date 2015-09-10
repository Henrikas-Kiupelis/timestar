package com.superum.helper;

import com.superum.api.v1.account.Account;
import com.superum.api.v2.exception.UnauthorizedRequestException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

/**
 * <pre>
 * Wraps Principal in order to provide methods for partitionIds/usernames
 *
 * Objects of this class should be autowired into @RequestMapping methods inside RestControllers
 * </pre>
 */
public class PartitionAccount {

    /**
     * @return partitionId of the user who is making the request
     */
    public int partitionId() {
        return partitionId;
    }

    /**
     * @return username of the user who is making the request; this does not contain partitionId
     */
    public String username() {
        return username;
    }

    /**
     * @return username of the user who is making the request; this DOES contain partitionId
     */
    public String accountUsername() {
        return usernameFor(username);
    }

    /**
     * @return username, modified to have the partitionId prefix; assumes the prefix is missing
     */
    public String usernameFor(String username) {
        return partitionId + SEPARATOR_STR + username;
    }

    /**
     * @return username of an account, modified to have the partitionId prefix; assumes the prefix is missing
     */
    public String usernameFor(Account account) {
        return usernameFor(account.getUsername());
    }

    /**
     * @return true if the given account belongs to the user who made the request, false otherwise
     */
    public boolean belongsTo(Account account) {
        return accountUsername().equals(account.getUsername());
    }

    @Override
    public String toString() {
        return accountUsername();
    }

    // CONSTRUCTORS

    public PartitionAccount(int partitionId, String username) {
        this.partitionId = partitionId;
        this.username = username;
    }

    public PartitionAccount(Principal user) {
        if (user == null)
            throw new UnauthorizedRequestException("Anonymous request to a method which requires authentication");

        String accountUsername = user.getName();
        int separatorIndex = accountUsername.indexOf(SEPARATOR);
        this.partitionId = Integer.parseInt(accountUsername.substring(0, separatorIndex));
        this.username = accountUsername.substring(separatorIndex + 1);
    }

    public PartitionAccount() {
        this(SecurityContextHolder.getContext().getAuthentication());
    }

    // PRIVATE

    private final int partitionId;
    private final String username;

    private static final char SEPARATOR = '.';
    private static final String SEPARATOR_STR = String.valueOf(SEPARATOR);

}
