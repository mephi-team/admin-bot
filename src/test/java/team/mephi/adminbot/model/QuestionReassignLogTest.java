package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для сущности {@link QuestionReassignLog}.
 */
class QuestionReassignLogTest {

    /**
     * Проверяет заполнение полей через билдер.
     */
    @Test
    void givenBuilder_WhenBuild_ThenFieldsAreSet() {
        // Arrange
        UserQuestion question = UserQuestion.builder().id(1L).build();
        Direction fromDirection = Direction.builder().id(2L).name("Old").build();
        Direction toDirection = Direction.builder().id(3L).name("New").build();
        User user = User.builder().id(4L).build();

        // Act
        QuestionReassignLog log = QuestionReassignLog.builder()
                .id(10L)
                .question(question)
                .fromDirection(fromDirection)
                .toDirection(toDirection)
                .reason("Причина")
                .reassignedBy(user)
                .build();

        // Assert
        assertEquals(10L, log.getId());
        assertEquals(question, log.getQuestion());
        assertEquals(fromDirection, log.getFromDirection());
        assertEquals(toDirection, log.getToDirection());
        assertEquals("Причина", log.getReason());
        assertEquals(user, log.getReassignedBy());
    }
}
