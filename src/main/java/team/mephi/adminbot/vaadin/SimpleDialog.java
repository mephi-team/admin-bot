package team.mephi.adminbot.vaadin;

import com.vaadin.flow.function.SerializableConsumer;

public interface SimpleDialog<T> {
    void showDialog(Object item, SerializableConsumer<T> consumer);
}
