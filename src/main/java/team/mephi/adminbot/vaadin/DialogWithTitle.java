package team.mephi.adminbot.vaadin;

import com.vaadin.flow.function.SerializableConsumer;

/**
 * Интерфейс для диалогов с заголовком.
 */
public interface DialogWithTitle {
    /**
     * Показывает диалог с переданным элементом и потребителем.
     *
     * @param item     элемент для отображения в диалоге
     * @param consumer потребитель для обработки результата диалога
     */
    void showDialog(Object item, SerializableConsumer<?> consumer);

    /**
     * Устанавливает заголовок диалога.
     *
     * @param title заголовок диалога
     */
    void setHeaderTitle(String title);
}
