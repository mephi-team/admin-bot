package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для Mailing.
 * Покрывают: сравнение рассылок по идентификатору.
 */
class MailingTest {

    /**
     * Проверяет равенство при совпадении идентификаторов.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        Mailing first = new Mailing();
        first.setId(1L);
        Mailing second = new Mailing();
        second.setId(1L);

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }

    /**
     * Проверяет неравенство при разных идентификаторах.
     */
    @Test
    void Given_differentId_When_equals_Then_returnsFalse() {
        // Arrange
        Mailing first = new Mailing();
        first.setId(1L);
        Mailing second = new Mailing();
        second.setId(2L);

        // Act
        boolean result = first.equals(second);

        // Assert
        assertFalse(result);
    }
}
