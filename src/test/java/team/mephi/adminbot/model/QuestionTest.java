package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности Question.
 */
class QuestionTest {

    @Test
    void onCreate_shouldSetCreatedAtToNow() {
        // given
        Question question = Question.builder()
                .questionText("Как поступить?")
                .answerText("Через сайт.")
                .build();

        assertNull(question.getCreatedAt());

        // when
        question.onCreate();

        // then
        assertNotNull(question.getCreatedAt());
        assertTrue(
                Duration.between(question.getCreatedAt(), LocalDateTime.now()).getSeconds() < 5
        );
    }

    @Test
    void onCreate_shouldOverrideExistingCreatedAt() {
        // given
        LocalDateTime oldTime = LocalDateTime.now().minusDays(3);

        Question question = Question.builder()
                .questionText("Вопрос")
                .answerText("Ответ")
                .createdAt(oldTime)
                .build();

        // when
        question.onCreate();

        // then
        assertNotEquals(oldTime, question.getCreatedAt());
    }
}
