package team.mephi.adminbot.vaadin.core;

/**
 * Интерфейс для компонентов, которые предоставляют провайдер данных типа T.
 *
 * @param <T> Тип провайдера данных.
 */
public interface HasDataProvider<T> {
    /**
     * Получает провайдер данных.
     *
     * @return Провайдер данных.
     */
    T getDataProvider();
}
