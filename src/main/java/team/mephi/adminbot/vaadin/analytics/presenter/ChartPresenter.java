package team.mephi.adminbot.vaadin.analytics.presenter;

import com.vaadin.flow.data.provider.Query;
import team.mephi.adminbot.vaadin.HasDataProvider;
import team.mephi.adminbot.vaadin.analytics.dataproviders.ChartDataProvider;
import team.mephi.adminbot.vaadin.analytics.views.ActivityView;

public class ChartPresenter implements ChartActions<ActivityView.SimpleData>, HasDataProvider<ChartDataProvider> {

    private final ChartDataProvider dataProvider;

    public ChartPresenter(ChartDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public void onUpdateFilter(ActivityView.SimpleData filter) {
        var item = dataProvider.fetch(new Query<>(filter));
        dataProvider.refreshItem(item.findFirst().orElse(null));
    }

    @Override
    public ChartDataProvider getDataProvider() {
        return this.dataProvider;
    }
}
