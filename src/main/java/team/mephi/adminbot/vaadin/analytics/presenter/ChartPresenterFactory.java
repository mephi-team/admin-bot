package team.mephi.adminbot.vaadin.analytics.presenter;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.ChartDataService;
import team.mephi.adminbot.vaadin.analytics.dataproviders.ChartDataProvider;

@SpringComponent
public class ChartPresenterFactory {

    private final ChartDataService chartDataService;

    public ChartPresenterFactory(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }

    public ChartActions<?> createPresenter() {
        return new ChartPresenter<>(new ChartDataProvider<ChartDataService>(chartDataService));
    }
}
