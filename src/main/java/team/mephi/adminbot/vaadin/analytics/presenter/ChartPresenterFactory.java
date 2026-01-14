package team.mephi.adminbot.vaadin.analytics.presenter;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.analytics.dataproviders.ChartDataProvider;

@SpringComponent
public class ChartPresenterFactory {
    public ChartActions<?> createPresenter() {
        return new ChartPresenter(new ChartDataProvider());
    }
}
