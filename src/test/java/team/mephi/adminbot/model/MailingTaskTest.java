package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для MailingTask.
 * Покрывают: сравнение задач по идентификатору.
 */
class MailingTaskTest {

    /**
     * Проверяет равенство задач при одинаковом идентификаторе.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        MailingTask first = MailingTask.builder().id(1L).build();
        MailingTask second = MailingTask.builder().id(1L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }

    /**
     * Проверяет неравенство задач при разных идентификаторах.
     */
    @Test
    void Given_differentId_When_equals_Then_returnsFalse() {
        // Arrange
        MailingTask first = MailingTask.builder().id(1L).build();
        MailingTask second = MailingTask.builder().id(2L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertFalse(result);
    }
}
