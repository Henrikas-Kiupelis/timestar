package com.superum.api.teacher;

import com.superum.helper.field.ManyDefined;
import com.superum.helper.validation.Validator;
import org.jooq.lambda.Seq;

import java.util.List;

import static com.superum.helper.validation.Validator.validate;

/**
 * <pre>
 * Domain object for teacher languages
 *
 * This object should be used to validate DTO data and use it in a meaningful manner; it encapsulates only the
 * specific version of DTO, which is used for commands
 * </pre>
 */
public final class ValidTeacherLanguages implements ManyDefined<Integer, String> {

    @Override
    public Integer primaryValue() {
        return id;
    }

    @Override
    public Seq<String> secondaryValues() {
        return Seq.seq(languages);
    }

    public boolean hasLanguages() {
        return languages != null;
    }

    public ValidTeacherLanguages withoutId() {
        return new ValidTeacherLanguages(null, languages);
    }

    public ValidTeacherLanguages withId(Integer id) {
        return new ValidTeacherLanguages(id, languages);
    }

    // CONSTRUCTORS

    public static ValidTeacherLanguages fromDTO(FullTeacherDTO fullTeacherDTO) {
        return new ValidTeacherLanguages(fullTeacherDTO.getId(), fullTeacherDTO.getLanguages());
    }

    private ValidTeacherLanguages(Integer id, List<String> languages) {
        validate(id).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidTeacherException("Teacher id can't be negative: " + id));

        validate(languages).Null().or().not().empty()
                .forEach(Validator::validate, language -> language.not().Null().not().blank().fits(LANGUAGE_CODE_SIZE_LIMIT)
                        .ifInvalid(() -> new InvalidTeacherException("Specific Teacher languages must not be null, blank or exceed "
                                + LANGUAGE_CODE_SIZE_LIMIT + " chars: " + language.value())))
                .ifInvalid(() -> new InvalidTeacherException("Teacher must have at least a single language!"));

        this.id = id;
        this.languages = languages;
    }

    // PRIVATE

    private final Integer id;
    private final List<String> languages;

    private static final int LANGUAGE_CODE_SIZE_LIMIT = 3;

}
