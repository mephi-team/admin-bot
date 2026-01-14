package team.mephi.adminbot.integration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * База для интеграционных тестов.
 * Поднимает полноценный Spring context + даёт MockMvc.
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationTestBase {
}
