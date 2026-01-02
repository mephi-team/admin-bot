package team.mephi.adminbot.vaadin.users.service;

import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleFile;
import team.mephi.adminbot.model.StoredFile;
import team.mephi.adminbot.repository.FileRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.users.components.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final Path storage = Paths.get("storage");

    public FileServiceImpl(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }
    @Override
    public void uploadAll(List<SimpleFile> files, String currentUser) throws IOException {
        LocalDate currentDate = LocalDate.now();
        Path dir = storage.resolve(currentDate.format(DateTimeFormatter.ofPattern("yy/MM/dd")));
        Files.createDirectories(dir);
        files.forEach(file -> {
            try {
                file.setContent(Files.move(file.getContent(), dir.resolve(UUID.randomUUID() + ".bin")));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        fileRepository.saveAll(files.stream().map(file -> StoredFile.builder()
                .filename(file.getName())
                .storagePath(file.getContent().toString())
                .mimeType(file.getType())
                .size(file.getSize())
                .uploadedBy(userRepository.findByEmail(currentUser).orElseThrow())
                .build()).toList());
    }
}
