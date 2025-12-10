package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности Question (проверка @PrePersist onCreate).
 */
class QuestionTest {

    @Test
    void onCreate_shouldSetCreatedAtIfNull() {
        // given
        Question question = Question.builder()
                .questionText("Как поступить в школу?")
                .answerText("Заполните заявку на сайте.")
                .build();

        assertNull(question.getCreatedAt(), "До вызова onCreate createdAt должен быть null");

        // when
        question.onCreate();

        // then
        assertNotNull(question.getCreatedAt(), "После onCreate createdAt должен быть установлен");
        assertTrue(
                Duration.between(question.getCreatedAt(), LocalDateTime.now()).getSeconds() < 5,
                "createdAt должен быть установлен примерно в текущее время"
        );
    }

    @Test
    void onCreate_shouldOverrideExistingCreatedAt() {
        // given
        LocalDateTime oldTime = LocalDateTime.now().minusDays(5);
        Question question = Question.builder()
                .questionText("Вопрос")
                .answerText("Ответ")
                .createdAt(oldTime)
                .build();

        // when
        question.onCreate();

        // then
        assertNotNull(question.getCreatedAt());
        assertNotEquals(oldTime, question.getCreatedAt(), "onCreate должен перезаписать createdAt");
    }
}
