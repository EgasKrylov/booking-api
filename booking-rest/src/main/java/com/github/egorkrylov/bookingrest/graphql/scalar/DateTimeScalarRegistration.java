package com.github.egorkrylov.bookingrest.graphql.scalar;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsRuntimeWiring;
import graphql.scalars.ExtendedScalars;
import graphql.schema.idl.RuntimeWiring;

@DgsComponent
public class DateTimeScalarRegistration {

    @DgsRuntimeWiring
    public RuntimeWiring.Builder addScalar(RuntimeWiring.Builder builder) {
        return builder
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.Date);
    }
}

