package com.github.egorkrylov.bookingrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = {"com.github.egorkrylov.bookingrest", "com.github.egorkrylov.bookingapicontract"}
)
public class BookingRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingRestApplication.class, args);
	}

}
