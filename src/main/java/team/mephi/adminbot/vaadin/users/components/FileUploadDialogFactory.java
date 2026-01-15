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

    public FileUploadDialogFactory(AuthService authService, FileService fileService) {
        this.authService = authService;
        this.fileService = fileService;
    }

    public FileUploadDialog create() {
        return new FileUploadDialog(authService, fileService);
    }
}
