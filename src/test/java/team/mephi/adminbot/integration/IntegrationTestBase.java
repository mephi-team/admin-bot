package team.mephi.adminbot.integration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * База для интеграционных тестов.
 * Поднимает полноценный Spring context + даёт MockMvc + PostgreSQL Testcontainers.
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public abstract class IntegrationTestBase {

    /**
     * PostgreSQL контейнер для интеграционных тестов.
     */
    @Container
    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:18-alpine")
                    .withDatabaseName("test_db")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        // Datasource
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES::getDriverClassName);

        // JPA (можешь подстроить под свою реальную стратегию)
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.open-in-view", () -> "false");

        // Чтобы тесты были тише/быстрее (опционально)
        registry.add("spring.jpa.show-sql", () -> "false");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "false");
    }
}
