package team.mephi.adminbot.dto;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Юнит-тесты для DialogWithLastMessageDto.
 * Покрывают: обработку типов даты последнего сообщения.
 */
class DialogWithLastMessageDtoTest {

    /**
     * Проверяет обработку значения Timestamp.
     */
    @Test
    void Given_timestamp_When_constructed_Then_setsInstant() {
        // Arrange
        Timestamp timestamp = Timestamp.from(Instant.parse("2024-01-01T10:00:00Z"));

        // Act
        DialogWithLastMessageDto dto = new DialogWithLastMessageDto(
                1L,
                "Last",
                "First",
                "Role",
                "ext",
                timestamp,
                0,
                "text",
                "USER",
                "Sender"
        );

        // Assert
        assertEquals(timestamp.toInstant(), dto.getLastMessageAt());
    }

    /**
     * Проверяет обработку значения LocalDateTime.
     */
    @Test
    void Given_localDateTime_When_constructed_Then_setsInstantUtc() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 2, 11, 0);

        // Act
        DialogWithLastMessageDto dto = new DialogWithLastMessageDto(
                2L,
                "Last",
                "First",
                "Role",
                "ext",
                dateTime,
                0,
                "text",
                "USER",
                "Sender"
        );

        // Assert
        assertEquals(dateTime.toInstant(ZoneOffset.UTC), dto.getLastMessageAt());
    }

    /**
     * Проверяет обработку значения Instant.
     */
    @Test
    void Given_instant_When_constructed_Then_setsInstant() {
        // Arrange
        Instant instant = Instant.parse("2024-01-03T12:00:00Z");

        // Act
        DialogWithLastMessageDto dto = new DialogWithLastMessageDto(
                3L,
                "Last",
                "First",
                "Role",
                "ext",
                instant,
                0,
                "text",
                "USER",
                "Sender"
        );

        // Assert
        assertEquals(instant, dto.getLastMessageAt());
    }

    /**
     * Проверяет установку null для неизвестного типа даты.
     */
    @Test
    void Given_unknownType_When_constructed_Then_setsNull() {
        // Arrange
        Object unknown = new Object();

        // Act
        DialogWithLastMessageDto dto = new DialogWithLastMessageDto(
                4L,
                "Last",
                "First",
                "Role",
                "ext",
                unknown,
                0,
                "text",
                "USER",
                "Sender"
        );

        // Assert
        assertNull(dto.getLastMessageAt());
    }
}
