package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.ScriptTaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Тесты для сущности {@link EnrollmentScriptFile}.
 */
class EnrollmentScriptFileTest {

    /**
     * Проверяет установку статуса по умолчанию при создании.
     */
    @Test
    void givenFileWithoutStatus_WhenOnCreateCalled_ThenStatusSetToPending() {
        // Arrange
        EnrollmentScriptFile file = EnrollmentScriptFile.builder().build();

        // Act
        ScriptTaskStatus statusBefore = file.getStatus();
        file.onCreate();
        ScriptTaskStatus statusAfter = file.getStatus();

        // Assert
        assertNull(statusBefore, "До вызова onCreate status должен быть null");
        assertEquals(ScriptTaskStatus.PENDING, statusAfter, "После onCreate status должен быть установлен в PENDING");
    }

    /**
     * Проверяет, что заданный статус не перезаписывается.
     */
    @Test
    void givenFileWithStatus_WhenOnCreateCalled_ThenStatusIsPreserved() {
        // Arrange
        EnrollmentScriptFile file = EnrollmentScriptFile.builder()
                .status(ScriptTaskStatus.RUNNING)
                .build();

        // Act
        file.onCreate();

        // Assert
        assertEquals(ScriptTaskStatus.RUNNING, file.getStatus(),
                "onCreate не должен перезаписывать status, если он уже задан");
    }
}
