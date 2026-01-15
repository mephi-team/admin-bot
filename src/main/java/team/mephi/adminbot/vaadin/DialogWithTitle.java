package team.mephi.adminbot.vaadin;

import com.vaadin.flow.function.SerializableConsumer;

/**
 * Интерфейс для диалогов с заголовком.
 */
public interface DialogWithTitle {
    void showDialog(Object item, SerializableConsumer<?> consumer);

    void setHeaderTitle(String title);
}
