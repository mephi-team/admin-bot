package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для UserAnswer.
 * Покрывают: сравнение ответов по идентификатору.
 */
class UserAnswerTest {

    /**
     * Проверяет равенство при одинаковых идентификаторах.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        UserAnswer first = UserAnswer.builder().id(1L).build();
        UserAnswer second = UserAnswer.builder().id(1L).build();

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
        UserAnswer first = UserAnswer.builder().id(1L).build();
        UserAnswer second = UserAnswer.builder().id(2L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertFalse(result);
    }
}
