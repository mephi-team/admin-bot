package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.function.SerializableConsumer;

public interface DialogService<T> {
    void showDialog(Object user, DialogType type, SerializableConsumer<T> callback);

    void showConfirmDialog(Object user, DialogType type, Icon icon, SerializableConsumer<T> callback);
}
