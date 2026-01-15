package team.mephi.adminbot.vaadin.analytics.dataproviders;

import com.vaadin.flow.data.provider.AbstractDataProvider;
import com.vaadin.flow.data.provider.Query;
import software.xdev.chartjs.model.data.BarData;
import team.mephi.adminbot.service.ChartDataService;
import team.mephi.adminbot.vaadin.analytics.views.ActivityView;
import team.mephi.adminbot.vaadin.analytics.views.OrdersView;
import team.mephi.adminbot.vaadin.analytics.views.PreordersView;
import team.mephi.adminbot.vaadin.analytics.views.UtmView;

import java.util.stream.Stream;

public class ChartDataProvider<T> extends AbstractDataProvider<BarData, T> {
    private final ChartDataService chartDataService;

    public ChartDataProvider(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }

    @Override
    public boolean isInMemory() {
        return false;
    }

    @Override
    public int size(Query<BarData, T> query) {
        return 0;
    }

    @Override
    public Stream<BarData> fetch(Query<BarData, T> query) {
        var filter = query.getFilter().orElse(null);
        if (filter instanceof ActivityView.ActivityFilterData) {
            return Stream.of(chartDataService.forActivity((ActivityView.ActivityFilterData) filter));
        } else if (filter instanceof PreordersView.PreorderFilterData) {
            return Stream.of(chartDataService.forPreorders((PreordersView.PreorderFilterData) filter));
        } else if (filter instanceof OrdersView.OrderFilterData) {
            return Stream.of(chartDataService.forOrders((OrdersView.OrderFilterData) filter));
        } else if (filter instanceof UtmView.UtmFilterData) {
            return Stream.of(chartDataService.forUtm((UtmView.UtmFilterData) filter));
        }
        return Stream.empty();
    }
}
