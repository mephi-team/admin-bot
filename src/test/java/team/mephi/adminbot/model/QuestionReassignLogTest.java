package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности QuestionReassignLog (проверка @PrePersist onCreate).
 */
class QuestionReassignLogTest {

    @Test
    void onCreate_shouldSetCreatedAtToNow() {
        // given
        QuestionReassignLog log = QuestionReassignLog.builder().build();

        assertNull(log.getCreatedAt(), "До onCreate createdAt должен быть null");

        // when
        log.onCreate();

        // then
        assertNotNull(log.getCreatedAt(), "После onCreate createdAt должен быть установлен");
        assertTrue(
                Duration.between(log.getCreatedAt(), LocalDateTime.now()).getSeconds() < 5,
                "createdAt должен быть примерно текущим временем"
        );
    }
}
