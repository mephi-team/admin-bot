package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.AuthService;
import team.mephi.adminbot.service.FileService;

/**
 * Фабрика для создания диалогов загрузки файлов.
 */
@SpringComponent
public class FileUploadDialogFactory {

    private final AuthService authService;
    private final FileService fileService;

    /**
     * Конструктор фабрики диалогов загрузки файлов.
     *
     * @param authService сервис аутентификации
     * @param fileService сервис работы с файлами
     */
    public FileUploadDialogFactory(AuthService authService, FileService fileService) {
        this.authService = authService;
        this.fileService = fileService;
    }

    /**
     * Создает диалоговое окно для загрузки файлов.
     *
     * @return созданное диалоговое окно для загрузки файлов
     */
    public FileUploadDialog create() {
        return new FileUploadDialog(authService, fileService);
    }
}
