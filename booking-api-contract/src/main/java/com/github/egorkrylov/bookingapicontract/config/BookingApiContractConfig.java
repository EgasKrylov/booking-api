package com.github.egorkrylov.bookingapicontract.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Hotel Reservation API",
                version = "1.0.0",
                description = """
                        REST API для бронирования номеров в отеле
                        """
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development")
        }
)

@SecurityScheme(
        name = BookingApiContractConfig.SECURITY_SHEMA_BEARER,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "JWT Bearer token. Пример: `Authorization: Bearer eyJhbGci...`"
)
public final class BookingApiContractConfig {

    public static final String SECURITY_SHEMA_BEARER = "bearerAuth";

    private BookingApiContractConfig() {
    }
}

