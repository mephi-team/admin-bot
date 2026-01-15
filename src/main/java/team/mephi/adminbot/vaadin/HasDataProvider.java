package team.mephi.adminbot.vaadin;

/**
 * Интерфейс для компонентов, которые предоставляют провайдер данных типа T.
 *
 * @param <T> Тип провайдера данных.
 */
public interface HasDataProvider<T> {
    T getDataProvider();
}
