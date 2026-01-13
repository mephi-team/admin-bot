package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.AnswerStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Юнит-тесты для сущности UserAnswer.
 */
class UserAnswerTest {

    @Test
    void equals_shouldReturnTrueForSameId() {
        // given
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

        // then
        assertEquals(answer1, answer2, "Ответы с одинаковым ID должны быть равны");
    }

    @Test
    void equals_shouldReturnFalseForDifferentIds() {
        // given
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

        // then
        assertNotEquals(answer1, answer2, "Ответы с разными ID не должны быть равны");
    }

    @Test
    void hashCode_shouldBeConsistent() {
        // given
        UserAnswer answer = UserAnswer.builder()
                .id(1L)
                .answerText("Answer")
                .status(AnswerStatus.DRAFT)
                .build();

        // when
        int hashCode1 = answer.hashCode();
        int hashCode2 = answer.hashCode();

        // then
        assertEquals(hashCode1, hashCode2, "hashCode должен быть консистентным");
    }
}
