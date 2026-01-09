package team.mephi.adminbot.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для enum MessageStatus.
 * Проверяет наличие всех ожидаемых значений и корректность сериализации/десериализации.
 */
class MessageStatusTest {

    @Test
    void messageStatus_shouldContainAllExpectedValues() {
        // given
        MessageStatus[] statuses = MessageStatus.values();

        // then
        assertEquals(4, statuses.length, "MessageStatus должен содержать 4 значения");
        
        // Проверяем наличие каждого ожидаемого статуса
        assertTrue(contains(statuses, MessageStatus.SENT), "MessageStatus должен содержать SENT");
        assertTrue(contains(statuses, MessageStatus.DELIVERED), "MessageStatus должен содержать DELIVERED");
        assertTrue(contains(statuses, MessageStatus.READ), "MessageStatus должен содержать READ");
        assertTrue(contains(statuses, MessageStatus.FAILED), "MessageStatus должен содержать FAILED");
    }

    @Test
    void valueOf_shouldReturnCorrectEnumForSENT() {
        // when
        MessageStatus status = MessageStatus.valueOf("SENT");

        // then
        assertEquals(MessageStatus.SENT, status);
        assertEquals("SENT", status.name());
    }

    @Test
    void valueOf_shouldReturnCorrectEnumForDELIVERED() {
        // when
        MessageStatus status = MessageStatus.valueOf("DELIVERED");

        // then
        assertEquals(MessageStatus.DELIVERED, status);
        assertEquals("DELIVERED", status.name());
    }

    @Test
    void valueOf_shouldReturnCorrectEnumForREAD() {
        // when
        MessageStatus status = MessageStatus.valueOf("READ");

        // then
        assertEquals(MessageStatus.READ, status);
        assertEquals("READ", status.name());
    }

    @Test
    void valueOf_shouldReturnCorrectEnumForFAILED() {
        // when
        MessageStatus status = MessageStatus.valueOf("FAILED");

        // then
        assertEquals(MessageStatus.FAILED, status);
        assertEquals("FAILED", status.name());
    }

    @Test
    void valueOf_shouldThrowExceptionForInvalidValue() {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> {
            MessageStatus.valueOf("INVALID_STATUS");
        }, "valueOf должен выбросить IllegalArgumentException для несуществующего значения");
    }

    @Test
    void valueOf_shouldBeCaseSensitive() {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> {
            MessageStatus.valueOf("sent");
        }, "valueOf должен быть чувствителен к регистру");
        
        assertThrows(IllegalArgumentException.class, () -> {
            MessageStatus.valueOf("Sent");
        }, "valueOf должен быть чувствителен к регистру");
    }

    @Test
    void enumRoundTrip_shouldWorkCorrectly() {
        // given
        for (MessageStatus originalStatus : MessageStatus.values()) {
            // when
            String name = originalStatus.name();
            MessageStatus parsedStatus = MessageStatus.valueOf(name);

            // then
            assertEquals(originalStatus, parsedStatus, 
                "Round-trip сериализация/десериализация должна работать для " + originalStatus);
        }
    }

    @Test
    void enumComparison_shouldWorkWithEquals() {
        // given
        MessageStatus status1 = MessageStatus.SENT;
        MessageStatus status2 = MessageStatus.valueOf("SENT");
        MessageStatus status3 = MessageStatus.DELIVERED;

        // then
        assertEquals(status1, status2, "Одинаковые enum значения должны быть равны");
        assertNotEquals(status1, status3, "Разные enum значения не должны быть равны");
        assertSame(status1, status2, "Enum значения должны быть одним и тем же объектом");
    }

    /**
     * Вспомогательный метод для проверки наличия значения в массиве enum.
     */
    private boolean contains(MessageStatus[] statuses, MessageStatus target) {
        for (MessageStatus status : statuses) {
            if (status == target) {
                return true;
            }
        }
        return false;
    }
}
