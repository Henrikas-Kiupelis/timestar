package com.superum.api.core;

import com.superum.api.exception.InvalidRequestException;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class CommonControllerLogic {

    protected void validateId(String source, int id) {
        if (id <= 0)
            throw new InvalidRequestException(source + " id must be positive, not: " + id);
    }

    /**
     * <pre>
     * Defaults are set up below rather than in annotation because otherwise
     *
     *      ...?page=&per_page=
     *
     * would cause a NumberFormatException instead of using default values; this is fixed by:
     * 1) setting the field to Integer rather than int, causing empty param to default to null rather than
     *    fail to parse an empty string;
     * 2) since null would now be a valid value, it would no longer be overridden by @RequestParam(defaultValue)
     *    and would instead propagate down;
     * </pre>
     */
    protected int validatePage(Integer page) {
        if (page == null)
            return DEFAULT_PAGE;

        if (page <= 0)
            throw new InvalidRequestException("Pages can only be positive, not " + page);

        return page - 1; //Pages start with 1 in the URL, but start with 0 in the app logic
    }

    /**
     * <pre>
     * Defaults are set up below rather than in annotation because otherwise
     *
     *      ...?page=&per_page=
     *
     * would cause a NumberFormatException instead of using default values; this is fixed by:
     * 1) setting the field to Integer rather than int, causing empty param to default to null rather than
     *    fail to parse an empty string;
     * 2) since null would now be a valid value, it would no longer be overridden by @RequestParam(defaultValue)
     *    and would instead propagate down;
     * </pre>
     */
    protected int validatePerPage(Integer per_page) {
        if (per_page == null)
            return DEFAULT_PER_PAGE;

        if (per_page <= 0 || per_page > 100)
            throw new InvalidRequestException("You can only request 1-100 items per page, not " + per_page);

        return per_page;
    }

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PER_PAGE = 25;

}
