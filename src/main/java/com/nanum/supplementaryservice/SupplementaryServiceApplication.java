package com.nanum.supplementaryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication(scanBasePackages = "com.nanum")
//@EnableDis
@EnableJpaAuditing
public class SupplementaryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplementaryServiceApplication.class, args);
    }

}
