package team.mephi.adminbot.vaadin.analytics.presenter;

import com.vaadin.flow.data.provider.Query;
import software.xdev.chartjs.model.data.BarData;
import team.mephi.adminbot.vaadin.HasDataProvider;
import team.mephi.adminbot.vaadin.analytics.dataproviders.ChartDataProvider;

public class ChartPresenter<T> implements ChartActions<T>, HasDataProvider<ChartDataProvider<T>> {

    private final ChartDataProvider<T> dataProvider;

    public ChartPresenter(ChartDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public void onUpdateFilter(T filter) {
        var item = dataProvider.fetch(new Query<BarData, T>(filter));
        dataProvider.refreshItem(item.findFirst().orElse(null));
    }

    @Override
    public ChartDataProvider<T> getDataProvider() {
        return this.dataProvider;
    }
}
