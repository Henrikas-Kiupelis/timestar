package com.superum.api.v2.account;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.InstantSerializer;
import com.google.common.base.MoreObjects;
import com.superum.api.v1.account.Account;
import com.superum.api.v1.account.AccountType;
import eu.goodlike.v2.validate.Validate;
import org.joda.time.Instant;

import java.util.Arrays;
import java.util.Objects;

import static eu.goodlike.misc.Constants.NOT_NULL_NOT_BLANK;

/**
 * <pre>
 * Describes an account, used to manage authentication and authorization internally
 *
 * When creating an instance of ValidAccountDTO with JSON, these fields are required:
 *      FIELD_NAME  : FIELD_DESCRIPTION                                         FIELD_CONSTRAINTS
 *      username    : username used by this account                             any String, max 60 chars
 *      password    : password used by this account                             any String/char array
 *
 * When building JSON, use format
 *      for single objects:  "FIELD_NAME":"VALUE"
 *      for lists:           "FIELD_NAME":["VALUE1", "VALUE2", ...]
 * If you omit a field, it will use null;
 *
 * Example of JSON to send:
 * {
 *      "username": "admin",
 *      "password": "nimda"
 * }
 *
 * When returning an instance of ValidAccountDTO with JSON, these additional fields will be present:
 *      FIELD_NAME  : FIELD_DESCRIPTION
 *      id          : id of this account, if one is associated;
 *                    this only applies to teacher accounts; for admins it is null
 *      accountType : "ADMIN" or "TEACHER"
 *      createdAt   : timestamp, taken by the database at the time of creation
 *      updatedAt   : timestamp, taken by the database at the time of creation and updating
 *
 * Password field will be omitted for security
 *
 * Example of JSON to expect:
 * {
 *      "id": null,
 *      "username": "admin",
 *      "accountType": "ADMIN",
 *      "createdAt": 1439413200000,
 *      "updatedAt": 1439455032324
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class ValidAccountDTO {

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("accountType")
    public String getAccountType() {
        return accountType == null ? null : accountType.name();
    }

    @JsonProperty("createdAt")
    @JsonSerialize(using = InstantSerializer.class)
    public Instant getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updatedAt")
    @JsonSerialize(using = InstantSerializer.class)
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @JsonIgnore
    public Account toAccount() {
        return new Account(id, username, accountType == null ? null : accountType.name(), password, createdAt, updatedAt);
    }

    public static void validateUsername(String username) {
        NOT_NULL_NOT_BLANK.isNoLargerThan(USERNAME_SIZE_LIMIT).ifInvalid(username)
                .thenThrow(() -> new InvalidAccountException("Account username must not be null, blank, or exceed " +
                        USERNAME_SIZE_LIMIT + " chars: " + username));
    }

    // CONSTRUCTORS

    @JsonCreator
    public static ValidAccountDTO jsonInstance(@JsonProperty("username") String username,
                                               @JsonProperty("password") char[] password) {
        validateUsername(username);
        Validate.charArray().not().isNull().not().isBlank().ifInvalid(password)
                .thenThrow(() -> new InvalidAccountException("Account must have a password!"));

        return new ValidAccountDTO(null, username, null, password, null, null);
    }

    public ValidAccountDTO(Integer id, String username, String accountType, char[] password, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.username = username;
        this.accountType = accountType == null ? null : AccountType.valueOf(accountType);
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ValidAccountDTO valueOf(Account account) {
        return new ValidAccountDTO(account.getId(), account.getUsername(), account.getAccountType(),
                account.getPasswordArray(), account.getCreatedAt(), account.getUpdatedAt());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static IdStep stepBuilder() {
        return new Builder();
    }

    // PRIVATE

    private final Integer id;
    private final String username;
    private final AccountType accountType;
    private char[] password;

    private final Instant createdAt;
    private final Instant updatedAt;

    private static final int USERNAME_SIZE_LIMIT = 190;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ValidAccountDTO")
                .add("Account id", id)
                .add("Username", username)
                .add("Account Type", accountType)
                .addValue("Password: [PROTECTED]")
                .add("Created at", createdAt)
                .add("Updated at", updatedAt)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidAccountDTO)) return false;
        ValidAccountDTO that = (ValidAccountDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(accountType, that.accountType) &&
                Arrays.equals(password, that.password) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, accountType, Arrays.asList(password));
    }

    // GENERATED

    public interface IdStep {
        UsernameStep id(Integer id);
        UsernameStep noId();
    }

    public interface UsernameStep {
        AccountTypeStep username(String username);
    }

    public interface AccountTypeStep {
        PasswordStep accountType(String accountType);
    }

    public interface PasswordStep {
        BuildStep password(char... password);
    }

    public interface BuildStep {
        BuildStep createdAt(Instant createdAt);
        BuildStep createdAt(long createdAt);
        BuildStep updatedAt(Instant updatedAt);
        BuildStep updatedAt(long updatedAt);
        ValidAccountDTO build();
    }

    public static class Builder implements IdStep, UsernameStep, AccountTypeStep, PasswordStep, BuildStep {
        private Integer id;
        private String username;
        private String accountType;
        private char[] password;

        private Instant createdAt;
        private Instant updatedAt;

        private Builder() {}

        @Override
        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder noId() {
            this.id = null;
            return this;
        }

        @Override
        public Builder username(String username) {
            this.username = username;
            return this;
        }

        @Override
        public Builder accountType(String accountType) {
            this.accountType = accountType;
            return this;
        }

        @Override
        public Builder password(char... password) {
            this.password = password;
            return this;
        }

        @Override
        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Override
        public Builder createdAt(long createdAt) {
            this.createdAt = new Instant(createdAt);
            return this;
        }

        @Override
        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @Override
        public Builder updatedAt(long updatedAt) {
            this.updatedAt = new Instant(updatedAt);
            return this;
        }

        @Override
        public ValidAccountDTO build() {
            return new ValidAccountDTO(id, username, accountType, password, createdAt, updatedAt);
        }
    }

}
