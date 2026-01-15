package team.mephi.adminbot.vaadin.analytics.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartActions;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.analytics.views.UtmView;

import static team.mephi.adminbot.vaadin.analytics.tabs.AnalyticsTabType.UTM;

/**
 * Провайдер вкладки "UTM" в разделе аналитики.
 */
@SpringComponent
public class UtmTabProvider implements AnalyticsTabProvider {

    private final CohortService cohortService;

    /**
     * Конструктор провайдера вкладки "UTM".
     *
     * @param cohortService сервис для работы с когортами пользователей
     */
    public UtmTabProvider(CohortService cohortService) {
        this.cohortService = cohortService;
    }

    @Override
    public AnalyticsTabType getTabId() {
        return UTM;
    }

    @Override
    public String getTabLabel() {
        return UTM.getTabLabelKey();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Component createTabContent(ChartActions<?> actions) {
        return new UtmView((ChartPresenter<UtmView.UtmFilterData>) actions, cohortService);
    }

    @Override
    public Integer getPosition() {
        return UTM.ordinal();
    }
}
