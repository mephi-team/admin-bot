package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.QuestionStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности UserQuestion (проверка @PrePersist/@PreUpdate и Lombok-методов).
 */
class UserQuestionTest {

    @Test
    void onCreate_shouldSetCreatedAtAndUpdatedAt() {
        // given
        UserQuestion q = UserQuestion.builder()
                .text("Q")
                .role("student")
                .status(QuestionStatus.NEW)
                .build();

        assertNull(q.getCreatedAt(), "До onCreate createdAt должен быть null");
        assertNull(q.getUpdatedAt(), "До onCreate updatedAt должен быть null");

        // when
        q.onCreate();

        // then
        assertNotNull(q.getCreatedAt());
        assertNotNull(q.getUpdatedAt());
        assertEquals(q.getCreatedAt(), q.getUpdatedAt(), "createdAt и updatedAt должны совпадать при создании");
    }

    @Test
    void onUpdate_shouldUpdateUpdatedAtOnly() throws InterruptedException {
        // given
        UserQuestion q = new UserQuestion();
        q.onCreate();

        LocalDateTime createdAtBefore = q.getCreatedAt();
        LocalDateTime updatedAtBefore = q.getUpdatedAt();

        Thread.sleep(50);

        // when
        q.onUpdate();

        // then
        assertEquals(createdAtBefore, q.getCreatedAt(), "createdAt не должен меняться");
        assertTrue(q.getUpdatedAt().isAfter(updatedAtBefore), "updatedAt должен обновиться");
    }

    @Test
    void builder_shouldSetAnswersList() {
        // given
        UserAnswer a1 = new UserAnswer();
        UserAnswer a2 = new UserAnswer();

        // when
        UserQuestion q = UserQuestion.builder()
                .id(1L)
                .answers(List.of(a1, a2))
                .status(QuestionStatus.PROGRESS)
                .text("T")
                .role("r")
                .build();

        // then
        assertEquals(1L, q.getId());
        assertEquals(2, q.getAnswers().size());
        assertEquals(QuestionStatus.PROGRESS, q.getStatus());
        assertNotNull(q.toString());
    }
}
