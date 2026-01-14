package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.NotificationStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Юнит-тесты для NotificationQueue.
 * Покрывают: установку статуса по умолчанию.
 */
class NotificationQueueTest {

    /**
     * Проверяет установку статуса PENDING при создании.
     */
    @Test
    void Given_nullStatus_When_onCreate_Then_setsPending() {
        // Arrange
        NotificationQueue queue = NotificationQueue.builder().build();

        // Act
        queue.onCreate();

        // Assert
        assertEquals(NotificationStatus.PENDING, queue.getStatus());
    }
}
