package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для Message.
 * Покрывают: сравнение сообщений по идентификатору.
 */
class MessageTest {

    /**
     * Проверяет равенство сообщений при одинаковом идентификаторе.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        Message first = new Message();
        first.setId(1L);
        Message second = new Message();
        second.setId(1L);

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }

    /**
     * Проверяет неравенство сообщений при разных идентификаторах.
     */
    @Test
    void Given_differentId_When_equals_Then_returnsFalse() {
        // Arrange
        Message first = new Message();
        first.setId(1L);
        Message second = new Message();
        second.setId(2L);

        // Act
        boolean result = first.equals(second);

        // Assert
        assertFalse(result);
    }
}
