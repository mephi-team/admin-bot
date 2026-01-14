package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.AnswerStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Тесты для сущности {@link UserAnswer}.
 */
class UserAnswerTest {

    /**
     * Проверяет равенство ответов с одинаковым идентификатором.
     */
    @Test
    void givenSameIds_WhenCompared_ThenEqualsReturnsTrue() {
        // Arrange
        UserAnswer answer1 = UserAnswer.builder()
                .id(1L)
                .answerText("Answer 1")
                .status(AnswerStatus.DRAFT)
                .build();

        UserAnswer answer2 = UserAnswer.builder()
                .id(1L)
                .answerText("Answer 2")
                .status(AnswerStatus.SENT)
                .build();

        // Act
        boolean equalsResult = answer1.equals(answer2);

        // Assert
        assertEquals(true, equalsResult, "Ответы с одинаковым ID должны быть равны");
    }

    /**
     * Проверяет, что ответы с разными идентификаторами не равны.
     */
    @Test
    void givenDifferentIds_WhenCompared_ThenEqualsReturnsFalse() {
        // Arrange
        UserAnswer answer1 = UserAnswer.builder()
                .id(1L)
                .answerText("Answer")
                .status(AnswerStatus.DRAFT)
                .build();

        UserAnswer answer2 = UserAnswer.builder()
                .id(2L)
                .answerText("Answer")
                .status(AnswerStatus.DRAFT)
                .build();

        // Act
        boolean equalsResult = answer1.equals(answer2);

        // Assert
        assertNotEquals(true, equalsResult, "Ответы с разными ID не должны быть равны");
    }

    /**
     * Проверяет консистентность hashCode.
     */
    @Test
    void givenAnswer_WhenHashCodeCalled_ThenConsistent() {
        // Arrange
        UserAnswer answer = UserAnswer.builder()
                .id(1L)
                .answerText("Answer")
                .status(AnswerStatus.DRAFT)
                .build();

        // Act
        int hashCode1 = answer.hashCode();
        int hashCode2 = answer.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2, "hashCode должен быть консистентным");
    }
}
