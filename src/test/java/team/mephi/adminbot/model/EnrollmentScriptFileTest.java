package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.ScriptTaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Юнит-тесты для EnrollmentScriptFile.
 * Покрывают: установку статуса при создании.
 */
class EnrollmentScriptFileTest {

    /**
     * Проверяет установку статуса по умолчанию при создании.
     */
    @Test
    void Given_nullStatus_When_onCreate_Then_setsPending() {
        // Arrange
        EnrollmentScriptFile file = EnrollmentScriptFile.builder().filename("file.xlsx").build();

        // Act
        file.onCreate();

        // Assert
        assertEquals(ScriptTaskStatus.PENDING, file.getStatus());
    }
}
