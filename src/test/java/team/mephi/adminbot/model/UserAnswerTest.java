package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности UserAnswer (проверка @PrePersist onCreate).
 */
class UserAnswerTest {

    @Test
    void onCreate_shouldSetAnsweredAtIfNull() {
        // given
        UserAnswer answer = UserAnswer.builder()
                .answerText("A")
                .status("ok")
                .build();

        assertNull(answer.getAnsweredAt(), "До onCreate answeredAt должен быть null");

        // when
        answer.onCreate();

        // then
        assertNotNull(answer.getAnsweredAt(), "После onCreate answeredAt должен быть установлен");
    }

    @Test
    void onCreate_shouldNotOverrideAnsweredAtIfAlreadySet() {
        // given
        LocalDateTime oldTime = LocalDateTime.now().minusDays(3);
        UserAnswer answer = UserAnswer.builder()
                .answerText("A")
                .status("ok")
                .answeredAt(oldTime)
                .build();

        // when
        answer.onCreate();

        // then
        assertEquals(oldTime, answer.getAnsweredAt(), "onCreate не должен перезаписывать answeredAt, если он уже задан");
    }
}
