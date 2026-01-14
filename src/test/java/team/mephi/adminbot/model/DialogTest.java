package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для Dialog.
 * Покрывают: сравнение диалогов по идентификатору.
 */
class DialogTest {

    /**
     * Проверяет равенство диалогов при одинаковом идентификаторе.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        Dialog first = new Dialog();
        first.setId(1L);
        Dialog second = new Dialog();
        second.setId(1L);

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }

    /**
     * Проверяет неравенство диалогов при разных идентификаторах.
     */
    @Test
    void Given_differentId_When_equals_Then_returnsFalse() {
        // Arrange
        Dialog first = new Dialog();
        first.setId(1L);
        Dialog second = new Dialog();
        second.setId(2L);

        // Act
        boolean result = first.equals(second);

        // Assert
        assertFalse(result);
    }
}
