package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для StudentTutor.
 * Покрывают: инициализацию полей и сравнение по идентификатору.
 */
class StudentTutorTest {

    /**
     * Проверяет установку даты и активности при создании.
     */
    @Test
    void Given_newAssignment_When_onCreate_Then_setsDefaults() {
        // Arrange
        StudentTutor assignment = StudentTutor.builder().build();
        assignment.setAssignedAt(null);
        assignment.setIsActive(null);

        // Act
        assignment.onCreate();

        // Assert
        assertNotNull(assignment.getAssignedAt());
        assertTrue(assignment.getIsActive());
    }

    /**
     * Проверяет равенство назначений по идентификатору.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        StudentTutor first = StudentTutor.builder().id(1L).build();
        StudentTutor second = StudentTutor.builder().id(1L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }

    /**
     * Проверяет неравенство назначений при разных идентификаторах.
     */
    @Test
    void Given_differentId_When_equals_Then_returnsFalse() {
        // Arrange
        StudentTutor first = StudentTutor.builder().id(1L).build();
        StudentTutor second = StudentTutor.builder().id(2L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertFalse(result);
    }
}
