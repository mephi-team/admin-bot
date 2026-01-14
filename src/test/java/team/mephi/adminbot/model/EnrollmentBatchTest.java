package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.MailingStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для сущности {@link EnrollmentBatch}.
 */
class EnrollmentBatchTest {

    /**
     * Проверяет, что при создании батча устанавливается статус по умолчанию.
     */
    @Test
    void givenBatchWithoutStatus_WhenOnCreateCalled_ThenStatusSetToDraft() {
        // Arrange
        EnrollmentBatch batch = EnrollmentBatch.builder().build();

        // Act
        batch.onCreate();

        // Assert
        assertEquals(MailingStatus.DRAFT, batch.getStatus());
    }
}
