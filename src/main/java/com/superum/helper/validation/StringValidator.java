package com.superum.helper.validation;

import java.util.Collection;

/**
 * <pre>
 * Validator implementation for strings
 *
 * Reasons for using this over @Valid:
 * 1) The validations seem all too basic, requiring either 3rd party annotations or custom made annotations for
 *    special cases; this seems daft, because it's simpler to just write plain old Java code; not to mention it
 *    is easier to debug this way;
 * 2) Copying the class for use in other projects, i.e. Android, libraries, etc, becomes cumbersome, because the
 *    dependencies of the annotations must be preserved; in most cases they are simply deleted;
 * 3) Allows for easier customization of error messages returned by the server, since custom exceptions can be
 *    thrown without significant hassle;
 * 4) Allows to test for incorrect input values prematurely as well, rather than waiting until an instance of the
 *    object was created, if such a need arises;
 *
 * Validation works by chaining predicates one after another; for example:
 *      validate(string).notNull().notEmpty().ifInvalid(...);
 *
 * The predicates are evaluated left to right, using normal boolean operator priority rules;
 * the default operator between two predicates is "and()";
 * you have to use "or()" explicitly; also, in order to simulate brackets, such like:
 *      predicate1 && (predicate2 || predicate3) && predicate4
 * you can use test() and testEnd():
 *      validate(string)
 *          .custom(predicate1)
 *          .test().custom(predicate2).or().custom(predicate3).testEnd()
 *          .custom(predicate4)
 *          .ifInvalid(...)
 * testEnd() can be omitted if it's the last call before ifInvalid()
 * </pre>
 */
public final class StringValidator extends Validator<String, StringValidator> {

    /**
     * Adds a predicate which tests if the string being validated is empty
     */
    public StringValidator empty() {
        registerCondition(String::isEmpty);
        return this;
    }

    /**
     * Adds a predicate which tests if the string being validated fits into a limited amount of characters
     */
    public StringValidator fits(int limit) {
        registerCondition(string -> string.length() <= limit);
        return this;
    }

    /**
     * Adds a predicate which tests if the string being validated belongs to a predetermined collection of Strings
     */
    public StringValidator matches(Collection<String> values) {
        registerCondition(values::contains);
        return this;
    }

    /**
     * Adds a predicate which tests if the string being validated contains only whitespace
     */
    public StringValidator blank() {
        registerCondition(string -> string.trim().isEmpty());
        return this;
    }

    /**
     * Adds a predicate which tests if the string being validated is e-mail; this check is not conclusive, but should
     * get rid of most obvious mistakes
     */
    public StringValidator email() {
        registerCondition(this::emailCheck);
        return this;
    }

    /**
     * Adds a predicate which tests if the string being validated is a comma separated list of positive numbers; it is
     * assumed that string itself was tested for blankness before calling this predicate
     */
    public StringValidator commaSeparatedListOfIntegers() {
        registerCondition(this::commasAndIntegers);
        return this;
    }

    // CONSTRUCTORS

    public StringValidator(String object) {
        super(object);
    }

    // PROTECTED

    @Override
    protected final StringValidator thisValidator() {
        return this;
    }

    @Override
    protected StringValidator newValidator() {
        return new StringValidator(object);
    }

    // PRIVATE

    /**
     * <pre>
     * Does some basic checking to filter out non e-mails; the following form is considered:
     *      PREFIX@DOMAIN
     * both PREFIX and DOMAIN cannot start with a dot, end with a dot, or contain multiple dots in a row
     * </pre>
     */
    private boolean emailCheck(String email) {
        int index = email.indexOf('@');
        return index >= 0 && dotCheck(email.substring(0, index).trim()) && dotCheck(email.substring(index + 1).trim());
    }

    /**
     * @return true if the string is not empty, doesn't start or end with a dot, and doesn't contain multiple dots in
     * a row; false otherwise
     */
    private boolean dotCheck(String string) {
        return !(string.isEmpty() || string.startsWith(".") || string.endsWith(".") || string.contains(".."));
    }

    /**
     * @return true if string is a comma separated list of positive integers, false otherwise
     */
    private boolean commasAndIntegers(String string) {
        if (string.startsWith(",") || string.endsWith(",") || string.contains(",,"))
            return false;

        for (char c : string.toCharArray())
            if (!(Character.isDigit(c) || c == ','))
                return false;

        return true;
    }

}
