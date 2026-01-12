package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.security.AuthenticationContext;
import team.mephi.adminbot.service.FileService;

@SpringComponent
public class FileUploadDialogFactory {

    private final AuthenticationContext authContext;
    private final FileService fileService;

    public FileUploadDialogFactory(AuthenticationContext authContext, FileService fileService) {
        this.authContext = authContext;
        this.fileService = fileService;
    }

    public FileUploadDialog create() {
        return new FileUploadDialog(authContext, fileService);
    }
}
