package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * Фабрика для создания диалогов блокировки пользователей.
 */
@SpringComponent
public class BlockDialogFactory {
    public BlockDialogFactory() {
    }

    public BlockDialog<?> create(Class<?> beanType) {
        return new BlockDialog<>(beanType);
    }
}
