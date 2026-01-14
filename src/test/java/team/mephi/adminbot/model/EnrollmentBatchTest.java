package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.MailingStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Юнит-тесты для EnrollmentBatch.
 * Покрывают: установку статуса при создании.
 */
class EnrollmentBatchTest {

    /**
     * Проверяет установку статуса по умолчанию при создании.
     */
    @Test
    void Given_nullStatus_When_onCreate_Then_setsDraft() {
        // Arrange
        EnrollmentBatch batch = EnrollmentBatch.builder().build();

        // Act
        batch.onCreate();

        // Assert
        assertEquals(MailingStatus.DRAFT, batch.getStatus());
    }
}
