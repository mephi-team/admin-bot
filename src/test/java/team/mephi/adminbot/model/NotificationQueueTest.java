package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.NotificationStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для сущности {@link NotificationQueue}.
 */
class NotificationQueueTest {

    /**
     * Проверяет установку статуса по умолчанию при создании.
     */
    @Test
    void givenQueueWithoutStatus_WhenOnCreateCalled_ThenStatusSetToPending() {
        // Arrange
        NotificationQueue queue = NotificationQueue.builder().build();

        // Act
        queue.onCreate();

        // Assert
        assertEquals(NotificationStatus.PENDING, queue.getStatus());
    }
}
