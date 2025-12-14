package team.mephi.adminbot.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

/**
 * Базовый класс для E2E-тестов: поднимаем полный Spring Boot контекст + PostgreSQL в Testcontainers.
 */
@Testcontainers
@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public abstract class BaseE2EIT {

    @Container
    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("admin_bot_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);

        // чтобы схема создавалась автоматически, если у тебя нет flyway/liquibase
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "false");
    }

    @BeforeEach
    void baseSetUp() {
        // можно оставить пустым; если нужно — здесь общий reset
    }
}
