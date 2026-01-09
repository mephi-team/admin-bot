package team.mephi.adminbot.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для enum NotificationStatus.
 * Проверяет наличие всех ожидаемых значений и корректность сериализации/десериализации.
 */
class NotificationStatusTest {

    @Test
    void notificationStatus_shouldContainAllExpectedValues() {
        // given
        NotificationStatus[] statuses = NotificationStatus.values();

        // then
        assertEquals(4, statuses.length, "NotificationStatus должен содержать 4 значения");
        
        // Проверяем наличие каждого ожидаемого статуса
        assertTrue(contains(statuses, NotificationStatus.PENDING), "NotificationStatus должен содержать PENDING");
        assertTrue(contains(statuses, NotificationStatus.SENT), "NotificationStatus должен содержать SENT");
        assertTrue(contains(statuses, NotificationStatus.FAILED), "NotificationStatus должен содержать FAILED");
        assertTrue(contains(statuses, NotificationStatus.DELIVERED), "NotificationStatus должен содержать DELIVERED");
    }

    @Test
    void valueOf_shouldReturnCorrectEnumForPENDING() {
        // when
        NotificationStatus status = NotificationStatus.valueOf("PENDING");

        // then
        assertEquals(NotificationStatus.PENDING, status);
        assertEquals("PENDING", status.name());
    }

    @Test
    void valueOf_shouldReturnCorrectEnumForSENT() {
        // when
        NotificationStatus status = NotificationStatus.valueOf("SENT");

        // then
        assertEquals(NotificationStatus.SENT, status);
        assertEquals("SENT", status.name());
    }

    @Test
    void valueOf_shouldReturnCorrectEnumForFAILED() {
        // when
        NotificationStatus status = NotificationStatus.valueOf("FAILED");

        // then
        assertEquals(NotificationStatus.FAILED, status);
        assertEquals("FAILED", status.name());
    }

    @Test
    void valueOf_shouldReturnCorrectEnumForDELIVERED() {
        // when
        NotificationStatus status = NotificationStatus.valueOf("DELIVERED");

        // then
        assertEquals(NotificationStatus.DELIVERED, status);
        assertEquals("DELIVERED", status.name());
    }

    @Test
    void valueOf_shouldThrowExceptionForInvalidValue() {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> {
            NotificationStatus.valueOf("INVALID_STATUS");
        }, "valueOf должен выбросить IllegalArgumentException для несуществующего значения");
    }

    @Test
    void valueOf_shouldBeCaseSensitive() {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> {
            NotificationStatus.valueOf("pending");
        }, "valueOf должен быть чувствителен к регистру");
        
        assertThrows(IllegalArgumentException.class, () -> {
            NotificationStatus.valueOf("Pending");
        }, "valueOf должен быть чувствителен к регистру");
    }

    @Test
    void enumRoundTrip_shouldWorkCorrectly() {
        // given
        for (NotificationStatus originalStatus : NotificationStatus.values()) {
            // when
            String name = originalStatus.name();
            NotificationStatus parsedStatus = NotificationStatus.valueOf(name);

            // then
            assertEquals(originalStatus, parsedStatus, 
                "Round-trip сериализация/десериализация должна работать для " + originalStatus);
        }
    }

    @Test
    void enumComparison_shouldWorkWithEquals() {
        // given
        NotificationStatus status1 = NotificationStatus.PENDING;
        NotificationStatus status2 = NotificationStatus.valueOf("PENDING");
        NotificationStatus status3 = NotificationStatus.SENT;

        // then
        assertEquals(status1, status2, "Одинаковые enum значения должны быть равны");
        assertNotEquals(status1, status3, "Разные enum значения не должны быть равны");
        assertSame(status1, status2, "Enum значения должны быть одним и тем же объектом");
    }

    /**
     * Вспомогательный метод для проверки наличия значения в массиве enum.
     */
    private boolean contains(NotificationStatus[] statuses, NotificationStatus target) {
        for (NotificationStatus status : statuses) {
            if (status == target) {
                return true;
            }
        }
        return false;
    }
}
