package team.mephi.adminbot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.mephi.adminbot.dto.SimpleFile;
import team.mephi.adminbot.model.Role;
import team.mephi.adminbot.model.StoredFile;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.FileRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link FileServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {
    @Mock
    private FileRepository fileRepository;

    @Mock
    private UserRepository userRepository;

    private FileServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new FileServiceImpl(fileRepository, userRepository);
    }

    @AfterEach
    void tearDown() throws IOException {
        Path storagePath = Path.of("storage");
        if (Files.exists(storagePath)) {
            try (var walk = Files.walk(storagePath)) {
                walk.sorted((a, b) -> b.compareTo(a))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
            }
        }
    }

    /**
     * Проверяет сохранение метаданных и перенос файла в хранилище.
     */
    @Test
    void givenFileUpload_WhenUploadAllCalled_ThenMetadataSaved() throws IOException {
        // Arrange
        Path tempFile = Files.createTempFile("upload", ".txt");
        Files.writeString(tempFile, "payload");
        SimpleFile input = SimpleFile.builder()
                .name("upload.txt")
                .type("text/plain")
                .size(Files.size(tempFile))
                .content(tempFile)
                .build();
        User uploader = User.builder().id(1L).email("user@example.com").role(Role.builder().code("ADMIN").build()).build();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(uploader));

        // Act
        service.uploadAll(List.of(input), "user@example.com");

        // Assert
        ArgumentCaptor<List<StoredFile>> captor = ArgumentCaptor.forClass(List.class);
        verify(fileRepository).saveAll(captor.capture());

        List<StoredFile> storedFiles = captor.getValue();
        assertThat(storedFiles).hasSize(1);
        StoredFile stored = storedFiles.getFirst();
        assertThat(stored.getFilename()).isEqualTo("upload.txt");
        assertThat(Path.of(stored.getStoragePath())).exists();
    }
}
