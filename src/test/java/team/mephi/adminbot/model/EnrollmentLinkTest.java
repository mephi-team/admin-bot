package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.model.enums.EnrollmentStatus;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для EnrollmentLink.
 * Покрывают: инициализацию статуса и валидацию даты истечения.
 */
class EnrollmentLinkTest {

    /**
     * Проверяет установку статуса и флага отправки при создании.
     */
    @Test
    void Given_newLink_When_onCreate_Then_setsDefaults() {
        // Arrange
        EnrollmentLink link = EnrollmentLink.builder()
                .link("link")
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .build();

        // Act
        link.onCreate();

        // Assert
        assertFalse(link.isSent());
        assertEquals(EnrollmentStatus.PENDING, link.getStatus());
    }

    /**
     * Проверяет выброс исключения при некорректной дате истечения.
     */
    @Test
    void Given_invalidExpiresAt_When_onUpdate_Then_throwsException() {
        // Arrange
        EnrollmentLink link = EnrollmentLink.builder()
                .link("link")
                .createdAt(Instant.parse("2024-01-02T10:00:00Z"))
                .expiresAt(Instant.parse("2024-01-01T10:00:00Z"))
                .build();

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, link::onUpdate);

        // Assert
        assertTrue(exception.getMessage().contains("expires_at"));
    }
}
