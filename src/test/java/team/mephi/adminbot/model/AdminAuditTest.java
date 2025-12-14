package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности AdminAudit (проверка @PrePersist onCreate).
 */
class AdminAuditTest {

    @Test
    void onCreate_shouldSetCreatedAt() {
        // given
        AdminAudit audit = AdminAudit.builder()
                .entityType("User")
                .build();

        assertNull(audit.getCreatedAt(), "До onCreate createdAt должен быть null");

        // when
        audit.onCreate();

        // then
        assertNotNull(audit.getCreatedAt(), "После onCreate createdAt должен быть установлен");
        assertTrue(
                Duration.between(audit.getCreatedAt(), LocalDateTime.now()).getSeconds() < 5,
                "createdAt должен быть примерно текущим временем"
        );
    }
}
