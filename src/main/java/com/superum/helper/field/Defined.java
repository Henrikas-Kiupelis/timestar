package com.superum.helper.field;

import com.superum.helper.field.core.MappedField;
import org.jooq.lambda.Seq;

/**
 * Allows defining class to table record mapping, and therefore automatic query creation using DefaultQueryMaker
 * @param <T> type of class that is mapped
 * @param <ID> type of primary key field
 */
public interface Defined<T extends Defined<T, ID>, ID> {

    /**
     * @return primary key field
     */
    MappedField<ID> primaryField();

    /**
     * @return sequence of fields that should be used when creating a record
     */
    Seq<MappedField<?>> createFields();

    /**
     * @return sequence of fields that should be used when updating a record
     */
    Seq<MappedField<?>> updateFields();

}
