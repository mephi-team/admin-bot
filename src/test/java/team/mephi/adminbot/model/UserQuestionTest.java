package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.QuestionStatus;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для сущности {@link UserQuestion}.
 */
class UserQuestionTest {

    /**
     * Проверяет установку дат при создании вопроса.
     */
    @Test
    void givenQuestionWithoutDates_WhenOnCreateCalled_ThenDatesAreInitialized() {
        // Arrange
        UserQuestion question = UserQuestion.builder()
                .text("Q")
                .role("student")
                .status(QuestionStatus.NEW)
                .build();

        // Act
        question.onCreate();

        // Assert
        assertNotNull(question.getCreatedAt());
        assertNotNull(question.getUpdatedAt());
        assertEquals(question.getCreatedAt(), question.getUpdatedAt(),
                "createdAt и updatedAt должны совпадать при создании");
    }

    /**
     * Проверяет обновление updatedAt при вызове onUpdate.
     */
    @Test
    void givenQuestion_WhenOnUpdateCalled_ThenUpdatedAtChanges() throws InterruptedException {
        // Arrange
        UserQuestion question = new UserQuestion();
        question.onCreate();
        Instant createdAtBefore = question.getCreatedAt();
        Instant updatedAtBefore = question.getUpdatedAt();

        // Act
        Thread.sleep(10);
        question.onUpdate();

        // Assert
        assertEquals(createdAtBefore, question.getCreatedAt(), "createdAt не должен меняться");
        assertTrue(question.getUpdatedAt().isAfter(updatedAtBefore), "updatedAt должен обновиться");
    }

    /**
     * Проверяет заполнение списка ответов через билдер.
     */
    @Test
    void givenBuilder_WhenAnswersProvided_ThenListIsStored() {
        // Arrange
        UserAnswer answer1 = new UserAnswer();
        UserAnswer answer2 = new UserAnswer();

        // Act
        UserQuestion question = UserQuestion.builder()
                .id(1L)
                .answers(List.of(answer1, answer2))
                .status(QuestionStatus.IN_PROGRESS)
                .text("T")
                .role("r")
                .build();

        // Assert
        assertEquals(1L, question.getId());
        assertEquals(2, question.getAnswers().size());
        assertEquals(QuestionStatus.IN_PROGRESS, question.getStatus());
        assertNotNull(question.toString());
    }
}
