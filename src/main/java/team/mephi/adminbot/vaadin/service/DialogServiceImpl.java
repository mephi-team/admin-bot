package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.function.SerializableConsumer;
import org.springframework.stereotype.Service;

@Service
public class DialogServiceImpl<T> implements DialogService<T> {

    private final DialogFactory dialogFactory;

    public DialogServiceImpl(DialogFactory dialogFactory) {
        this.dialogFactory = dialogFactory;
    }

    @Override
    public void showDialog(Object item, String label, SerializableConsumer<T> callback) {
        dialogFactory.getDialog(label).showDialog(item, callback);
    }

    @Override
    public void showConfirmDialog(Object item, String label, Icon icon, SerializableConsumer<T> callback) {
        dialogFactory.getConfirmDialog(label, icon).showForConfirm(item, () -> callback.accept((T) item));
    }
}
