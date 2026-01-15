package team.mephi.adminbot.vaadin;

import com.vaadin.flow.component.Component;

/**
 * Интерфейс для провайдера вкладок в пользовательском интерфейсе.
 *
 * @param <T> Тип действий, связанных с вкладкой.
 * @param <K> Тип идентификатора вкладки.
 */
public interface TabProvider<T, K> {
    K getTabId();

    String getTabLabel();

    Component createTabContent(T actions);

    Integer getPosition();
}
