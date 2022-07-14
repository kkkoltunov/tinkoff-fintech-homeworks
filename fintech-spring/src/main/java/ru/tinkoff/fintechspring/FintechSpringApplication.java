package ru.tinkoff.fintechspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class FintechSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(FintechSpringApplication.class, args);
    }
}
