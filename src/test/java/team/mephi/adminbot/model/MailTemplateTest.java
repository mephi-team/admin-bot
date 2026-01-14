package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для MailTemplate.
 * Покрывают: сравнение шаблонов по идентификатору.
 */
class MailTemplateTest {

    /**
     * Проверяет равенство шаблонов при одинаковом идентификаторе.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        MailTemplate first = new MailTemplate();
        first.setId(1L);
        MailTemplate second = new MailTemplate();
        second.setId(1L);

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }

    /**
     * Проверяет неравенство шаблонов при разных идентификаторах.
     */
    @Test
    void Given_differentId_When_equals_Then_returnsFalse() {
        // Arrange
        MailTemplate first = new MailTemplate();
        first.setId(1L);
        MailTemplate second = new MailTemplate();
        second.setId(2L);

        // Act
        boolean result = first.equals(second);

        // Assert
        assertFalse(result);
    }
}
