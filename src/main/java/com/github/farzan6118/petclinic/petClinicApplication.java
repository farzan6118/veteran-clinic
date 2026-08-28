package com.github.farzan6118.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class petClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(petClinicApplication.class, args);
    }

}
