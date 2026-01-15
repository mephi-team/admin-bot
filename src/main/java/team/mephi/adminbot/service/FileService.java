package team.mephi.adminbot.service;

import team.mephi.adminbot.dto.SimpleFile;

import java.io.IOException;
import java.util.List;

/**
 * Сервис для управления файлами.
 */
public interface FileService {
    void uploadAll(List<SimpleFile> files, String currentUser) throws IOException;
}
