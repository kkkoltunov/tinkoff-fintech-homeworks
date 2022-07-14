package ru.tinkoff.fintechspringrest;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import ru.tinkoff.fintechspringrest.cache.CourseCache;

import java.util.stream.Stream;

@SpringBootTest
public class ApplicationIntegrationTest {

    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:11.1");

    static {
        Startables.deepStart(Stream.of(postgresContainer)).join();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @Autowired
    private Flyway flyway;

    @Autowired
    private CourseCache courseCache;

    public void cleanAndMigrate() {
        flyway.clean();
        flyway.migrate();
        courseCache.deleteCache(1);
    }
}