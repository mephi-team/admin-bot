package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности NotificationQueue (проверка @PrePersist onCreate).
 */
class NotificationQueueTest {

    @Test
    void onCreate_shouldSetCreatedAtIfNull() {
        // given
        NotificationQueue queue = NotificationQueue.builder().build();

        assertNull(queue.getCreatedAt(), "До onCreate createdAt должен быть null");

        // when
        queue.onCreate();

        // then
        assertNotNull(queue.getCreatedAt(), "После onCreate createdAt должен быть установлен");
    }

    @Test
    void onCreate_shouldNotOverrideExistingCreatedAt() {
        // given
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        NotificationQueue queue = NotificationQueue.builder()
                .createdAt(time)
                .build();

        // when
        queue.onCreate();

        // then
        assertEquals(
                time,
                queue.getCreatedAt(),
                "onCreate не должен перезаписывать существующий createdAt"
        );
    }
}
