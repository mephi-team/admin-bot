package team.mephi.adminbot.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для сущности {@link StoredFile}.
 */
class FileTest {

    /**
     * Проверяет возможность установки времени загрузки файла.
     */
    @Test
    void givenStoredFile_WhenUploadedAtSet_ThenTimestampIsStored() {
        // Arrange
        StoredFile file = StoredFile.builder()
                .filename("a.txt")
                .mimeType("text/plain")
                .storagePath("/tmp/a.txt")
                .size(10L)
                .build();
        Instant now = Instant.now();

        // Act
        file.setUploadedAt(now);

        // Assert
        assertNotNull(file.getUploadedAt(), "uploadedAt должен быть установлен");
        assertTrue(
                Duration.between(file.getUploadedAt(), Instant.now()).getSeconds() < 5,
                "uploadedAt должен быть примерно текущим временем"
        );
    }

    /**
     * Проверяет заполнение основных полей через билдер.
     */
    @Test
    void givenBuilder_WhenBuild_ThenMainFieldsAreSet() {
        // Arrange
        Instant uploadedAt = Instant.now();

        // Act
        StoredFile file = StoredFile.builder()
                .id(1L)
                .filename("b.pdf")
                .mimeType("application/pdf")
                .storagePath("/files/b.pdf")
                .size(123L)
                .uploadedAt(uploadedAt)
                .build();

        // Assert
        assertEquals(1L, file.getId());
        assertEquals("b.pdf", file.getFilename());
        assertEquals("application/pdf", file.getMimeType());
        assertEquals("/files/b.pdf", file.getStoragePath());
        assertEquals(123L, file.getSize());
        assertNotNull(file.toString());
    }

    /**
     * Проверяет сравнение файлов по идентификатору.
     */
    @Test
    void givenFilesWithSameId_WhenCompared_ThenEqualityUsesId() {
        // Arrange
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

        // Act
        boolean equalsSameId = file1.equals(file2);
        boolean equalsDifferentId = file1.equals(file3);

        // Assert
        assertEquals(true, equalsSameId, "Файлы с одинаковым id должны быть равны");
        assertEquals(file1.hashCode(), file2.hashCode(), "Хеш-коды файлов с одинаковым id должны совпадать");
        assertNotEquals(true, equalsDifferentId, "Файлы с разным id не должны быть равны");
    }
}
