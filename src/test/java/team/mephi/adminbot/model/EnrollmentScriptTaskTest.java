package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для EnrollmentScriptTask.
 * Покрывают: сравнение задач по идентификатору.
 */
class EnrollmentScriptTaskTest {

    /**
     * Проверяет равенство при одинаковых идентификаторах.
     */
    @Test
    void Given_sameId_When_equals_Then_returnsTrue() {
        // Arrange
        EnrollmentScriptTask first = EnrollmentScriptTask.builder().id(1L).build();
        EnrollmentScriptTask second = EnrollmentScriptTask.builder().id(1L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertTrue(result);
    }

    /**
     * Проверяет неравенство при разных идентификаторах.
     */
    @Test
    void Given_differentId_When_equals_Then_returnsFalse() {
        // Arrange
        EnrollmentScriptTask first = EnrollmentScriptTask.builder().id(1L).build();
        EnrollmentScriptTask second = EnrollmentScriptTask.builder().id(2L).build();

        // Act
        boolean result = first.equals(second);

        // Assert
        assertFalse(result);
    }
}
