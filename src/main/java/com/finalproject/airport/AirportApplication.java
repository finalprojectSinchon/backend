package com.finalproject.airport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AirportApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirportApplication.class, args);
    }

}
