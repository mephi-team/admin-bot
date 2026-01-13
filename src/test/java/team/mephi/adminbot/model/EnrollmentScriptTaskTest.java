package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Тесты для сущности {@link EnrollmentScriptTask}.
 */
class EnrollmentScriptTaskTest {

    /**
     * Проверяет значения счётчиков по умолчанию.
     */
    @Test
    void givenNewTask_WhenCreated_ThenCountersDefaultToZero() {
        // Arrange
        EnrollmentScriptTask task = new EnrollmentScriptTask();

        // Act
        Integer successCount = task.getSuccessCount();
        Integer errorCount = task.getErrorCount();

        // Assert
        assertNotNull(successCount, "successCount не должен быть null");
        assertNotNull(errorCount, "errorCount не должен быть null");
        assertEquals(0, successCount, "successCount по умолчанию должен быть 0");
        assertEquals(0, errorCount, "errorCount по умолчанию должен быть 0");
    }
}
