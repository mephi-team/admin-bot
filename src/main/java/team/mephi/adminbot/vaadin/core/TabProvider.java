package team.mephi.adminbot.vaadin.core;

import com.vaadin.flow.component.Component;

/**
 * Интерфейс для провайдера вкладок в пользовательском интерфейсе.
 *
 * @param <T> Тип действий, связанных с вкладкой.
 * @param <K> Тип идентификатора вкладки.
 */
public interface TabProvider<T, K> {
    /**
     * Получает идентификатор вкладки.
     *
     * @return K - идентификатор вкладки.
     */
    K getTabId();

    /**
     * Получает метку вкладки.
     *
     * @return String - метка вкладки.
     */
    String getTabLabel();

    /**
     * Создает содержимое вкладки.
     *
     * @param actions Действия, связанные с вкладкой.
     * @return Component - содержимое вкладки.
     */
    Component createTabContent(T actions);

    /**
     * Получает позицию вкладки.
     *
     * @return Integer - позиция вкладки.
     */
    Integer getPosition();
}
