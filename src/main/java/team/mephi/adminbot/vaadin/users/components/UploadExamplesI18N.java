package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.upload.UploadI18N;

import java.util.Arrays;

/**
 * Provides a default I18N configuration for the Upload examples
 *
 * At the moment the Upload component requires a fully configured I18N instance,
 * even for use-cases where you only want to change individual texts.
 *
 * This I18N configuration is an adaption of the web components I18N defaults
 * and can be used as a basis for customizing individual texts.
 */
public class UploadExamplesI18N extends UploadI18N {
    public UploadExamplesI18N() {
        setDropFiles(new DropFiles().setOne("Drop file here")
                .setMany("Перетащите файлы сюда"));
        setAddFiles(new AddFiles().setOne("Upload File...")
                .setMany("Выберите файлы..."));
        setError(new Error().setTooManyFiles("Too Many Files.")
                .setFileIsTooBig("File is Too Big.")
                .setIncorrectFileType("Incorrect File Type."));
        setUploading(new Uploading()
                .setStatus(new Uploading.Status().setConnecting("Подключение...")
                        .setStalled("Stalled")
                        .setProcessing("Загрузка файла...").setHeld("В очереди"))
                .setRemainingTime(new Uploading.RemainingTime()
                        .setPrefix("оставшееся время: ")
                        .setUnknown("неизвестное оставшееся время"))
                .setError(new Uploading.Error()
                        .setServerUnavailable(
                                "Загрузка не удалась, пожалуйста, попробуйте позже.")
                        .setUnexpectedServerError(
                                "Загрузка не удалась из-за ошибки сервера.")
                        .setForbidden("Upload forbidden")));
        setUnits(new Units().setSize(Arrays.asList("B", "kB", "MB", "GB", "TB",
                "PB", "EB", "ZB", "YB")));
    }
}