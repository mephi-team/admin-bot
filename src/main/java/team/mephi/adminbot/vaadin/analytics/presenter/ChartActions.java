package team.mephi.adminbot.vaadin.analytics.presenter;

/**
 * Интерфейс для обработки действий с графиками на странице аналитики.
 *
 * @param <T> тип фильтра, используемого для обновления графиков
 */
public interface ChartActions<T> {
    void onUpdateFilter(T filter);
}
