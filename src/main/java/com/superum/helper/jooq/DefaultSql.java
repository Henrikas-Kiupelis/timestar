package com.superum.helper.jooq;

import org.jooq.Condition;
import org.jooq.Record;

public interface DefaultSql<R extends Record, ID> {

    Condition partitionId(int partitionId);

    Condition idAndPartition(ID id, int partitionId);

}
