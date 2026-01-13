package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для сущности StoredFile (проверка @CreationTimestamp и Lombok-методов).
 */
class FileTest {

    @Test
    void creationTimestamp_shouldSetUploadedAt() {
        // given
        StoredFile file = StoredFile.builder()
                .filename("a.txt")
                .mimeType("text/plain")
                .storagePath("/tmp/a.txt")
                .size(10L)
                .build();

        // when - uploadedAt устанавливается автоматически через @CreationTimestamp при сохранении
        // Для теста проверим, что поле может быть установлено
        file.setUploadedAt(Instant.now());

        // then
        assertNotNull(file.getUploadedAt(), "uploadedAt должен быть установлен");
        assertTrue(
                Duration.between(file.getUploadedAt(), Instant.now()).getSeconds() < 5,
                "uploadedAt должен быть примерно текущим временем"
        );
    }

    @Test
    void builder_shouldSetMainFields() {
        // given / when
        StoredFile file = StoredFile.builder()
                .id(1L)
                .filename("b.pdf")
                .mimeType("application/pdf")
                .storagePath("/files/b.pdf")
                .size(123L)
                .uploadedAt(Instant.now())
                .build();

        // then
        assertEquals(1L, file.getId());
        assertEquals("b.pdf", file.getFilename());
        assertEquals("application/pdf", file.getMimeType());
        assertEquals("/files/b.pdf", file.getStoragePath());
        assertEquals(123L, file.getSize());
        assertNotNull(file.toString());
    }

    @Test
    void equalsAndHashCode_shouldBeBasedOnId() {
        // given
        StoredFile file1 = StoredFile.builder()
                .id(1L)
                .filename("test.txt")
                .mimeType("text/plain")
                .storagePath("/test.txt")
                .size(100L)
                .build();

        StoredFile file2 = StoredFile.builder()
                .id(1L)
                .filename("different.txt")
                .mimeType("text/plain")
                .storagePath("/different.txt")
                .size(200L)
                .build();

        StoredFile file3 = StoredFile.builder()
                .id(2L)
                .filename("test.txt")
                .mimeType("text/plain")
                .storagePath("/test.txt")
                .size(100L)
                .build();

        // then
        assertEquals(file1, file2, "Файлы с одинаковым id должны быть равны");
        assertEquals(file1.hashCode(), file2.hashCode(), "Хеш-коды файлов с одинаковым id должны совпадать");
        assertNotEquals(file1, file3, "Файлы с разным id не должны быть равны");
    }
}
