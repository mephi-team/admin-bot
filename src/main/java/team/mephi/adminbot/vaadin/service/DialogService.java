package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.function.SerializableConsumer;

public interface DialogService<T> {
    void showDialog(Object user, String label, SerializableConsumer<T> callback);
    void showConfirmDialog(Object user, String label, SerializableConsumer<T> callback);
}
