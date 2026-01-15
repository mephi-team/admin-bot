package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.function.SerializableConsumer;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса для отображения диалогов различных типов.
 */
@Service
public class DialogServiceImpl<T> implements DialogService<T> {

    private final DialogFactory dialogFactory;

    /**
     * Конструктор сервиса диалогов.
     *
     * @param dialogFactory Фабрика для создания диалогов.
     */
    public DialogServiceImpl(DialogFactory dialogFactory) {
        this.dialogFactory = dialogFactory;
    }

    @Override
    public void showDialog(Object item, DialogType type, SerializableConsumer<T> callback) {
        dialogFactory.getDialog(type).showDialog(item, callback);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void showConfirmDialog(Object item, DialogType type, Icon icon, SerializableConsumer<T> callback) {
        dialogFactory.getConfirmDialog(type, icon).showForConfirm(item, () -> callback.accept((T) item));
    }
}
