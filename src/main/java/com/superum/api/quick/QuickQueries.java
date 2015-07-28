package com.superum.api.quick;

import com.superum.helper.QuickForeignKeyUsageChecker;
import com.superum.helper.QuickIdExistenceChecker;
import org.jooq.Record;

public abstract class QuickQueries<TableRecord extends Record, ID> {

    /**
     * @return true if a record with primary key id exists in a given partition; false otherwise
     */
    public final boolean existsQuick(ID id, int partitionId) {
        return existenceChecker.check(id, partitionId);
    }

    /**
     * @return true if a record with foreign key id exists in a given partition; false otherwise
     */
    public final boolean usedQuick(ID id, int partitionId) {
        return foreignKeyUsageChecker.check(id, partitionId);
    }

    // CONSTRUCTORS

    protected QuickQueries(QuickIdExistenceChecker<TableRecord, ID> existenceChecker, QuickForeignKeyUsageChecker<ID> foreignKeyUsageChecker) {
        this.existenceChecker = existenceChecker;
        this.foreignKeyUsageChecker = foreignKeyUsageChecker;
    }

    // PRIVATE

    private final QuickIdExistenceChecker<TableRecord, ID> existenceChecker;
    private final QuickForeignKeyUsageChecker<ID> foreignKeyUsageChecker;


}
