package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.jspecify.annotations.NonNull;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import team.mephi.adminbot.dto.SimpleFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileUploadDialog extends Dialog {
    private final Button addButton = new Button("Добавить пользователей");
    private final Map<String, SimpleFile> fileList = new HashMap<>();

    public FileUploadDialog(AuthenticationContext authContext, FileService fileService) {
        var user = authContext.getAuthenticatedUser(DefaultOidcUser.class).orElseThrow();
        setHeaderTitle("Добавление пользователей из файла");

        add(getUpload());

        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> {
            try {
                fileService.uploadAll(fileList.values().stream().toList(), user.getUserInfo().getEmail());
                fileList.clear();
                close();
                addButton.setEnabled(!fileList.isEmpty());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        addButton.setEnabled(false);

        getFooter().add(addButton);
    }

    private @NonNull Upload getUpload() {
        Upload dropEnabledUpload = new Upload();

        dropEnabledUpload.setI18n(new UploadExamplesI18N());
        dropEnabledUpload.addAllFinishedListener(e -> addButton.setEnabled(!fileList.isEmpty()));
        dropEnabledUpload.addFileRemovedListener(e -> {
            fileList.remove(e.getFileName());
            addButton.setEnabled(!fileList.isEmpty());
        });
        dropEnabledUpload.setUploadHandler(UploadHandler.toTempFile((metadata, file) -> {
            fileList.put(metadata.fileName(), SimpleFile.builder()
                    .name(metadata.fileName())
                    .type(metadata.contentType())
                    .size(metadata.contentLength())
                    .content(file.toPath())
                    .build());
            System.out.printf("File saved to: %s%n", file.getAbsolutePath());
        }));
        return dropEnabledUpload;
    }
}
