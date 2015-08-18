package com.superum.helper.field;

import org.jooq.lambda.Seq;

public interface ManyDefined<Primary, Secondary> {

    Primary primaryValue();

    Seq<Secondary> secondaryValues();

}
