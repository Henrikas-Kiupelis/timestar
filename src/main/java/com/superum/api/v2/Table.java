package com.superum.api.v2;

import java.util.Optional;
import java.util.stream.Stream;

public enum Table {

    teacher, customer, group, student, lesson;

    public static Optional<Table> forName(String name) {
        return Stream.of(Table.values()).filter(tables -> tables.name().equals(name)).findAny();
    }

}
