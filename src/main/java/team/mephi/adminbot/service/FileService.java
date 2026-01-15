package team.mephi.adminbot.service;

import team.mephi.adminbot.dto.SimpleFile;

import java.io.IOException;
import java.util.List;

/**
 * Сервис для управления файлами.
 */
public interface FileService {
    /**
     * Загружает все переданные файлы.
     *
     * @param files       список файлов для загрузки.
     * @param currentUser имя текущего пользователя, выполняющего загрузку.
     * @throws IOException если произошла ошибка ввода-вывода во время загрузки файлов.
     */
    void uploadAll(List<SimpleFile> files, String currentUser) throws IOException;
}
