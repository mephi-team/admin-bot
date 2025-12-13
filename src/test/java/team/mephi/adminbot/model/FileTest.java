package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности File (проверка @PrePersist onCreate и Lombok-методов).
 */
class FileTest {

    @Test
    void onCreate_shouldSetUploadedAt() {
        // given
        File file = File.builder()
                .filename("a.txt")
                .storagePath("/tmp/a.txt")
                .size(10L)
                .build();

        assertNull(file.getUploadedAt(), "До onCreate uploadedAt должен быть null");

        // when
        file.onCreate();

        // then
        assertNotNull(file.getUploadedAt(), "После onCreate uploadedAt должен быть установлен");
        assertTrue(
                Duration.between(file.getUploadedAt(), LocalDateTime.now()).getSeconds() < 5,
                "uploadedAt должен быть примерно текущим временем"
        );
    }

    @Test
    void builder_shouldSetMainFields() {
        // given / when
        File file = File.builder()
                .id(1L)
                .filename("b.pdf")
                .mimeType("application/pdf")
                .storagePath("/files/b.pdf")
                .size(123L)
                .build();

        // then
        assertEquals(1L, file.getId());
        assertEquals("b.pdf", file.getFilename());
        assertEquals("application/pdf", file.getMimeType());
        assertEquals("/files/b.pdf", file.getStoragePath());
        assertEquals(123L, file.getSize());
        assertNotNull(file.toString());
    }
}
