package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.server.streams.UploadHandler;
import org.jspecify.annotations.NonNull;
import team.mephi.adminbot.dto.SimpleFile;
import team.mephi.adminbot.service.AuthService;
import team.mephi.adminbot.service.FileService;
import team.mephi.adminbot.vaadin.components.RightDrawer;
import team.mephi.adminbot.vaadin.components.buttons.PrimaryButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Диалог для загрузки файлов.
 */
public class FileUploadDialog extends RightDrawer {
    private final Button addButton = new PrimaryButton(getTranslation("dialog_users_file_upload_action"));
    private final Map<String, SimpleFile> fileList = new HashMap<>();

    /**
     * Конструктор диалога загрузки файлов.
     *
     * @param authService сервис аутентификации
     * @param fileService сервис работы с файлами
     */
    public FileUploadDialog(AuthService authService, FileService fileService) {
        setHeaderTitle(getTranslation("dialog_users_file_upload_title"));

        add(getUpload());

        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(ignoredEvent -> {
            try {
                fileService.uploadAll(fileList.values().stream().toList(), authService.getUserInfo().getEmail());
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

    /**
     * Создает компонент загрузки файлов с поддержкой перетаскивания.
     *
     * @return компонент загрузки файлов
     */
    private @NonNull Upload getUpload() {
        Upload dropEnabledUpload = new Upload();

        dropEnabledUpload.setUploadButton(new SecondaryButton(getTranslation("dialog_users_file_upload_add_many")));

        var i18n = new UploadI18N();
        i18n.setDropFiles(new UploadI18N.DropFiles()
                .setOne(getTranslation("dialog_users_file_upload_drop_one"))
                .setMany(getTranslation("dialog_users_file_upload_drop_many")));
        i18n.setAddFiles(new UploadI18N.AddFiles()
                .setOne(getTranslation("dialog_users_file_upload_add_one"))
                .setMany(getTranslation("dialog_users_file_upload_add_many")));
        i18n.setUploading(new UploadI18N.Uploading()
                .setStatus(new UploadI18N.Uploading.Status()
                        .setConnecting(getTranslation("dialog_users_file_upload_connecting"))
                        .setStalled(getTranslation("dialog_users_file_upload_stalled"))
                        .setProcessing(getTranslation("dialog_users_file_upload_processing"))
                        .setHeld(getTranslation("dialog_users_file_upload_held")))
                .setRemainingTime(new UploadI18N.Uploading.RemainingTime()
                        .setPrefix(getTranslation("dialog_users_file_upload_remaining_prefix"))
                        .setUnknown(getTranslation("dialog_users_file_upload_remaining_unknown")))
                .setError(new UploadI18N.Uploading.Error()
                        .setServerUnavailable(getTranslation("dialog_users_file_upload_unavailable"))
                        .setUnexpectedServerError(getTranslation("dialog_users_file_upload_unexpected"))
                        .setForbidden(getTranslation("dialog_users_file_upload_forbidden"))));
        i18n.setUnits(new UploadI18N.Units()
                .setSize(Arrays.asList("B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")));

        dropEnabledUpload.setI18n(i18n);
        dropEnabledUpload.addAllFinishedListener(ignoredEvent -> addButton.setEnabled(!fileList.isEmpty()));
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
