package com.superum.helper.validation;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <pre>
 * Validator implementation for Lists
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
 * @param <T> type of object inside the List
 */
public final class ListValidator<T> extends Validator<List<T>, ListValidator<T>> {

    /**
     * Adds a predicate which tests if the list being validated is empty
     */
    public ListValidator<T> empty() {
        registerCondition(List::isEmpty);
        return this;
    }

    /**
     * Adds a predicate which tests if every element in the list is valid
     * @param validatorCreator function which creates a Validator for type T (usually Validator::validate);
     * @param invalidAction validation statement, for example:
     *      stringValidator -> stringValidator.notNull().notEmpty().ifInvalid(...);
     */
    public <V extends Validator<T, V>> ListValidator<T> forEach(Function<T, V> validatorCreator, Consumer<V> invalidAction) {
        registerCondition(list -> {
            list.stream()
                    .map(validatorCreator::apply)
                    .forEach(invalidAction);
            return true;
        });
        return this;
    }

    // CONSTRUCTORS

    protected ListValidator(List<T> object) {
        super(object);
    }

    // PROTECTED

    @Override
    protected ListValidator<T> thisValidator() {
        return this;
    }

    @Override
    protected ListValidator<T> newValidator() {
        return new ListValidator<>(object);
    }

}
