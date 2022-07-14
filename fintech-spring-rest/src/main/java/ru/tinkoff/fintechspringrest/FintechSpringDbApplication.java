package ru.tinkoff.fintechspringrest;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.fintechspringrest.dao.StudentEventRepository;
import ru.tinkoff.fintechspringrest.event.Status;
import ru.tinkoff.fintechspringrest.event.StudentEvent;
import ru.tinkoff.fintechspringrest.model.Student;
import ru.tinkoff.fintechspringrest.service.StudentService;

@SpringBootApplication
@EnableScheduling
public class FintechSpringDbApplication {

    public static void main(String[] args) {
        System.setProperty("java.security.auth.login.config", "src/main/resources/jaas.conf");
        SpringApplication.run(FintechSpringDbApplication.class, args);
    }
}
