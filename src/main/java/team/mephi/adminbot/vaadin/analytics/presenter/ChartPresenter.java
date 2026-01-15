package team.mephi.adminbot.vaadin.analytics.presenter;

import com.vaadin.flow.data.provider.Query;
import team.mephi.adminbot.vaadin.core.HasDataProvider;
import team.mephi.adminbot.vaadin.analytics.dataproviders.ChartDataProvider;

/**
 * Презентер для обработки действий с графиками на странице аналитики.
 *
 * @param <T> тип фильтра, используемого для обновления графиков
 */
public class ChartPresenter<T> implements ChartActions<T>, HasDataProvider<ChartDataProvider<T>> {

    private final ChartDataProvider<T> dataProvider;

    /**
     * Конструктор презентера.
     *
     * @param dataProvider провайдер данных для графиков
     */
    public ChartPresenter(ChartDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public void onUpdateFilter(T filter) {
        var item = dataProvider.fetch(new Query<>(filter));
        dataProvider.refreshItem(item.findFirst().orElse(null));
    }

    @Override
    public ChartDataProvider<T> getDataProvider() {
        return this.dataProvider;
    }
}
