package com.superum.helper.validation;

/**
 * <pre>
 * Validator implementation for char arrays
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
 *
 * At the moment there is no way to negate the next condition, but I'll consider it if it's needed
 * </pre>
 */
public class CharArrayValidator extends Validator<char[], CharArrayValidator> {

    /**
     * Adds a predicate which tests if the char array being validated is empty
     */
    public CharArrayValidator empty() {
        registerCondition(array -> array.length == 0);
        return this;
    }

    /**
     * Adds a predicate which tests if the char array being validated is not empty
     */
    public CharArrayValidator notEmpty() {
        registerCondition(array -> array.length > 0);
        return this;
    }

    /**
     * Adds a predicate which tests if the char array being validated fits into a limited amount of characters
     */
    public CharArrayValidator fits(int limit) {
        registerCondition(array -> array.length <= limit);
        return this;
    }

    // CONSTRUCTORS

    public CharArrayValidator(char[] object) {
        super(object);
    }

    // PROTECTED

    @Override
    protected CharArrayValidator thisValidator() {
        return this;
    }

    @Override
    protected CharArrayValidator newValidator() {
        return new CharArrayValidator(object);
    }

}
