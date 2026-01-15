package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для UserQuestion.
 * Покрывают: инициализацию дат и сравнение по идентификатору.
 */
class UserQuestionTest {

    /**
     * Проверяет заполнение дат при создании.
     */
    @Test
    void Given_newQuestion_When_onCreate_Then_setsDates() {
        // Arrange
        UserQuestion question = UserQuestion.builder().build();
        question.setCreatedAt(null);
        question.setUpdatedAt(null);

        // Act
        question.onCreate();

        // Assert
        assertNotNull(question.getCreatedAt());
        assertNotNull(question.getUpdatedAt());
    }

    /**
     * Проверяет обновление даты изменения.
     */
    @Test
    void Given_question_When_onUpdate_Then_updatesUpdatedAt() {
        // Arrange
        UserQuestion question = UserQuestion.builder().build();
        question.setUpdatedAt(Instant.parse("2024-01-01T00:00:00Z"));

        // Act
        question.onUpdate();

        // Assert
        assertNotNull(question.getUpdatedAt());
    }

    /**
     * Проверяет равенство вопросов по идентификатору.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        UserQuestion first = UserQuestion.builder().id(1L).build();
        UserQuestion second = UserQuestion.builder().id(1L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }

    /**
     * Проверяет неравенство вопросов при разных идентификаторах.
     */
    @Test
    void Given_differentId_When_equals_Then_returnsFalse() {
        // Arrange
        UserQuestion first = UserQuestion.builder().id(1L).build();
        UserQuestion second = UserQuestion.builder().id(2L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertFalse(result);
    }
}
