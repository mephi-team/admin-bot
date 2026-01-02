package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class FileUploadDialogFactory {
    public FileUploadDialogFactory() {}
    public FileUploadDialog create() {
        return new FileUploadDialog();
    }
}
