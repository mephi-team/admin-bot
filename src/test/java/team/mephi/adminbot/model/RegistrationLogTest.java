package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности RegistrationLog (проверка @PrePersist onCreate).
 */
class RegistrationLogTest {

    @Test
    void onCreate_shouldSetCreatedAt() {
        // given
        RegistrationLog log = RegistrationLog.builder().build();

        assertNull(log.getCreatedAt(), "До onCreate createdAt должен быть null");

        // when
        log.onCreate();

        // then
        assertNotNull(log.getCreatedAt(), "После onCreate createdAt должен быть установлен");
    }
}
