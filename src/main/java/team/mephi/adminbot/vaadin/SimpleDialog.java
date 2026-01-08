package team.mephi.adminbot.vaadin;

import com.vaadin.flow.function.SerializableConsumer;

public interface SimpleDialog {
    void showDialog(Object item, SerializableConsumer<?> consumer);
    void setHeaderTitle(String title);
}
