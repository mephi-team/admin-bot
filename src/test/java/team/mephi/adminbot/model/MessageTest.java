package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Тесты для сущности {@link Message}.
 */
class MessageTest {

    /**
     * Проверяет сравнение сообщений по идентификатору.
     */
    @Test
    void givenMessagesWithIds_WhenCompared_ThenEqualityUsesId() {
        // Arrange
        Message first = new Message();
        first.setId(1L);

        Message second = new Message();
        second.setId(1L);

        Message third = new Message();
        third.setId(2L);

        // Act
        boolean equalSame = first.equals(second);
        boolean equalDifferent = first.equals(third);

        // Assert
        assertEquals(true, equalSame);
        assertNotEquals(true, equalDifferent);
    }
}
