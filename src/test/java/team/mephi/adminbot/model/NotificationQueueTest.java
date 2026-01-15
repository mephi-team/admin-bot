package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.NotificationStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    // ===== Тесты статусов уведомлений =====

    @Test
    void onCreate_shouldSetDefaultStatusToPENDING() {
        // given
        NotificationQueue queue = NotificationQueue.builder().build();

        // when
        queue.onCreate();

        // then
        assertEquals(NotificationStatus.PENDING, queue.getStatus(),
                "По умолчанию статус должен быть PENDING");
    }

    @Test
    void onCreate_shouldNotOverrideExistingStatus() {
        // given
        NotificationQueue queue = NotificationQueue.builder()
                .status(NotificationStatus.SENT)
                .build();

        // when
        queue.onCreate();

        // then
        assertEquals(NotificationStatus.SENT, queue.getStatus(),
                "onCreate не должен перезаписывать существующий статус");
    }

    @Test
    void notificationQueue_shouldAcceptPENDINGStatus() {
        // given / when
        NotificationQueue queue = NotificationQueue.builder()
                .status(NotificationStatus.PENDING)
                .build();

        // then
        assertEquals(NotificationStatus.PENDING, queue.getStatus(),
                "NotificationQueue должен принимать статус PENDING");
    }

    @Test
    void notificationQueue_shouldAcceptSENTStatus() {
        // given / when
        NotificationQueue queue = NotificationQueue.builder()
                .status(NotificationStatus.SENT)
                .build();

        // then
        assertEquals(NotificationStatus.SENT, queue.getStatus(),
                "NotificationQueue должен принимать статус SENT");
    }

    @Test
    void notificationQueue_shouldAcceptFAILEDStatus() {
        // given / when
        NotificationQueue queue = NotificationQueue.builder()
                .status(NotificationStatus.FAILED)
                .build();

        // then
        assertEquals(NotificationStatus.FAILED, queue.getStatus(),
                "NotificationQueue должен принимать статус FAILED");
    }

    @Test
    void notificationQueue_shouldAcceptDELIVEREDStatus() {
        // given / when
        NotificationQueue queue = NotificationQueue.builder()
                .status(NotificationStatus.DELIVERED)
                .build();

        // then
        assertEquals(NotificationStatus.DELIVERED, queue.getStatus(),
                "NotificationQueue должен принимать статус DELIVERED");
    }

    @Test
    void notificationQueueWithFAILEDStatus_shouldHaveErrorMessage() {
        // given / when
        NotificationQueue queue = NotificationQueue.builder()
                .status(NotificationStatus.FAILED)
                .error("SMTP connection failed: timeout")
                .build();

        // then
        assertEquals(NotificationStatus.FAILED, queue.getStatus());
        assertEquals("SMTP connection failed: timeout", queue.getError(),
                "При статусе FAILED должна заполняться причина ошибки");
    }

    @Test
    void notificationQueueWithSENTStatus_canHaveNullError() {
        // given / when
        NotificationQueue queue = NotificationQueue.builder()
                .status(NotificationStatus.SENT)
                .error(null)
                .build();

        // then
        assertEquals(NotificationStatus.SENT, queue.getStatus());
        assertNull(queue.getError(),
                "Для статуса SENT поле error может быть null");
    }

    @Test
    void notificationQueueWithPENDINGStatus_canHaveNullError() {
        // given / when
        NotificationQueue queue = NotificationQueue.builder()
                .status(NotificationStatus.PENDING)
                .error(null)
                .build();

        // then
        assertEquals(NotificationStatus.PENDING, queue.getStatus());
        assertNull(queue.getError(),
                "Для статуса PENDING поле error может быть null");
    }

    @Test
    void notificationQueueWithDELIVEREDStatus_canHaveNullError() {
        // given / when
        NotificationQueue queue = NotificationQueue.builder()
                .status(NotificationStatus.DELIVERED)
                .error(null)
                .build();

        // then
        assertEquals(NotificationStatus.DELIVERED, queue.getStatus());
        assertNull(queue.getError(),
                "Для статуса DELIVERED поле error может быть null");
    }

    @Test
    void notificationQueue_shouldAllowStatusTransitionFromPENDINGToFAILED() {
        // given
        NotificationQueue queue = NotificationQueue.builder()
                .status(NotificationStatus.PENDING)
                .build();

        // when
        queue.setStatus(NotificationStatus.FAILED);
        queue.setError("Network unreachable");

        // then
        assertEquals(NotificationStatus.FAILED, queue.getStatus());
        assertEquals("Network unreachable", queue.getError());
    }

    @Test
    void notificationQueue_shouldAllowStatusTransitionFromPENDINGToSENT() {
        // given
        NotificationQueue queue = NotificationQueue.builder()
                .status(NotificationStatus.PENDING)
                .build();

        // when
        queue.setStatus(NotificationStatus.SENT);

        // then
        assertEquals(NotificationStatus.SENT, queue.getStatus());
    }
}
