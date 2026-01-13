package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.EnrollmentStatus;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тесты для сущности {@link EnrollmentLink}.
 */
class EnrollmentLinkTest {

    /**
     * Проверяет, что при создании ссылки выставляются значения по умолчанию.
     */
    @Test
    void givenLinkWithoutStatus_WhenOnCreateCalled_ThenDefaultsApplied() {
        // Arrange
        EnrollmentLink link = EnrollmentLink.builder()
                .sent(true)
                .build();

        // Act
        link.onCreate();

        // Assert
        assertFalse(link.isSent());
        assertEquals(EnrollmentStatus.PENDING, link.getStatus());
    }

    /**
     * Проверяет, что некорректная дата истечения вызывает исключение.
     */
    @Test
    void givenInvalidExpiration_WhenOnUpdateCalled_ThenExceptionThrown() {
        // Arrange
        Instant createdAt = Instant.now();
        EnrollmentLink link = EnrollmentLink.builder()
                .createdAt(createdAt)
                .expiresAt(createdAt.minusSeconds(60))
                .build();

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, link::onUpdate);

        // Assert
        assertEquals("expires_at must be after created_at. created_at: " + createdAt + ", expires_at: " + link.getExpiresAt(),
                exception.getMessage());
    }
}
