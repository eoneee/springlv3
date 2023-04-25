package com.example.springlv3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Springlv3Application {

    public static void main(String[] args) {
        SpringApplication.run(Springlv3Application.class, args);
    }

}
