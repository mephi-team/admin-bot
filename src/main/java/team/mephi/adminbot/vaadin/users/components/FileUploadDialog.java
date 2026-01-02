package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileUploadDialog extends Dialog {
    private final Button addButton = new Button("Добавить пользователей");
    private final Map<String, File> fileList = new HashMap<>();

    public FileUploadDialog() {
        setHeaderTitle("Добавление пользователей из файла");

        add(getUpload());

        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> {
            fileList.clear();
            close();
            addButton.setEnabled(!fileList.isEmpty());
        });
        addButton.setEnabled(false);

        getFooter().add(addButton);
    }

    private @NonNull Upload getUpload() {
        Upload dropEnabledUpload = new Upload();

        dropEnabledUpload.setI18n(new UploadExamplesI18N());
        dropEnabledUpload.addAllFinishedListener(e -> {
            addButton.setEnabled(!fileList.isEmpty());
        });
        dropEnabledUpload.addFileRemovedListener((e) -> {
            fileList.remove(e.getFileName());
            addButton.setEnabled(!fileList.isEmpty());
        });
        dropEnabledUpload.setUploadHandler(UploadHandler.toTempFile((metadata, file) -> {
            fileList.put(metadata.fileName(), file);
            System.out.printf("File saved to: %s%n", file.getAbsolutePath());
        }));
        return dropEnabledUpload;
    }
}
