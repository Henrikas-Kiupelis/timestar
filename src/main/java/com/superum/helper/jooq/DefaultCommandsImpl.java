package com.superum.helper.jooq;

import com.superum.helper.NullChecker;
import com.superum.helper.field.Defined;
import com.superum.helper.field.core.MappedField;
import org.jooq.*;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Transactional
public class DefaultCommandsImpl<R extends Record, ID> extends DefaultSqlImpl<R, ID> implements DefaultCommands<R, ID> {

    @Override
    public <T extends Defined<T, ID>, DTO> Optional<DTO> create(T body, int partitionId, Function<R, DTO> mapper) {
        NullChecker.check(body, mapper).notNull("Body and mapper function cannot be null");

        return body.createFields().foldLeft(sql.insertInto(table).set(partitionField, partitionId),
                (step, field) -> field.insert(step))
                .returning().fetch().stream().findFirst()
                .map(mapper);
    }

    @Override
    public <T extends Defined<T, ID>> int update(T body, int partitionId) {
        if (body == null)
            throw new IllegalArgumentException("Body value cannot be null");

        Tuple2<Optional<MappedField<?>>, Seq<MappedField<?>>> updateFields = body.updateFields().splitAtHead();

        UpdateSetFirstStep<R> updateStepFirst = sql.update(table);
        UpdateSetMoreStep<R> updateStepMore = updateFields.v1.map(field -> field.update(updateStepFirst))
                .orElseThrow(() -> new IllegalArgumentException("The updated body should have at least one field definition!"));

        return updateFields.v2.foldLeft(updateStepMore, (step, field) -> field.update(step))
                .where(idAndPartition(body.primaryField().getValue(), partitionId))
                .execute();
    }

    @Override
    public int delete(ID id, int partitionId) {
        if (id == null)
            throw new IllegalArgumentException("Primary key value cannot be null");

        return sql.deleteFrom(table)
                .where(idAndPartition(id, partitionId))
                .execute();
    }

    // CONSTRUCTORS

    public DefaultCommandsImpl(DSLContext sql, Table<R> table, TableField<R, ID> keyField, TableField<R, Integer> partitionField) {
        super(sql, table, keyField, partitionField);
    }

}
