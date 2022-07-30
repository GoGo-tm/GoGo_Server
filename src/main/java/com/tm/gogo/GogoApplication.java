package com.tm.gogo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GogoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GogoApplication.class, args);
    }

}
