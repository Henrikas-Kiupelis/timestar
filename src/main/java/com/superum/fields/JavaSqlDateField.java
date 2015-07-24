package com.superum.fields;

import com.superum.utils.ObjectUtils;

import java.sql.Date;

/**
 * <pre>
 * Specialized NamedField, intended to be used with java.sql.Date
 *
 * This Field will check for String equality between java.sql.Date instances;
 * normal equality can fail due to timezone differences
 * </pre>
 */
public class JavaSqlDateField extends SimpleField<Date> {

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof JavaSqlDateField))
            return false;

        JavaSqlDateField other = (JavaSqlDateField) o;

        return ObjectUtils.equalsJavaSqlDate(this.value, other.value);
    }

    // CONSTRUCTORS

    public JavaSqlDateField(String fieldName, Date value, Mandatory mandatory) {
        super(fieldName, value, mandatory);
    }

}
