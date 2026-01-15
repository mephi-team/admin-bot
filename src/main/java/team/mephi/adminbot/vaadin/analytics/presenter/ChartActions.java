package team.mephi.adminbot.vaadin.analytics.presenter;

/**
 * Интерфейс для обработки действий с графиками на странице аналитики.
 *
 * @param <T> тип фильтра, используемого для обновления графиков
 */
public interface ChartActions<T> {
    /**
     * Метод вызывается при обновлении фильтра для графиков.
     *
     * @param filter новый фильтр
     */
    void onUpdateFilter(T filter);
}
