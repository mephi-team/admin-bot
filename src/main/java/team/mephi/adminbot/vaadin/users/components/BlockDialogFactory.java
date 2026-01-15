package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * Фабрика для создания диалогов блокировки пользователей.
 */
@SpringComponent
public class BlockDialogFactory {
    /**
     * Конструктор фабрики диалогов блокировки пользователей.
     */
    public BlockDialogFactory() {
    }

    /**
     * Создает диалоговое окно для блокировки пользователя.
     *
     * @param beanType класс типа объекта, связанного с блокировкой
     * @return созданное диалоговое окно для блокировки пользователя
     */
    public BlockDialog<?> create(Class<?> beanType) {
        return new BlockDialog<>(beanType);
    }
}
