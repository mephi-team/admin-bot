package team.mephi.adminbot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.mephi.adminbot.dto.SimpleFile;
import team.mephi.adminbot.model.StoredFile;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.FileRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для FileServiceImpl.
 * Покрывают: загрузку файлов и сохранение метаданных.
 */
@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {
    @TempDir
    Path tempDir;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserRepository userRepository;

    @AfterEach
    void cleanupStorage() throws IOException {
        // Arrange
        Path storage = Path.of("storage");
        if (Files.exists(storage)) {
            try (var walk = Files.walk(storage)) {
                walk.sorted((a, b) -> b.compareTo(a)).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                    }
                });
            }
        }
    }

    /**
     * Проверяет сохранение файла и метаданных.
     */
    @Test
    void Given_files_When_uploadAll_Then_movesFilesAndSavesMetadata() throws IOException {
        // Arrange
        Path source = Files.createFile(tempDir.resolve("source.bin"));
        Files.writeString(source, "data");
        SimpleFile simpleFile = SimpleFile.builder()
                .name("source.bin")
                .type("application/octet-stream")
                .size(Files.size(source))
                .content(source)
                .build();
        when(userRepository.findByEmail(eq("user@example.com"))).thenReturn(java.util.Optional.of(User.builder().id(1L).build()));
        FileServiceImpl service = new FileServiceImpl(fileRepository, userRepository);

        // Act
        service.uploadAll(List.of(simpleFile), "user@example.com");

        // Assert
        assertTrue(Files.exists(simpleFile.getContent()));
        ArgumentCaptor<List<StoredFile>> captor = ArgumentCaptor.forClass(List.class);
        verify(fileRepository).saveAll(captor.capture());
        StoredFile stored = captor.getValue().getFirst();
        assertEquals("source.bin", stored.getFilename());
        assertEquals("application/octet-stream", stored.getMimeType());
    }
}
