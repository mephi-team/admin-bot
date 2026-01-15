package team.mephi.adminbot.vaadin.analytics.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartActions;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.analytics.views.PreordersView;

import static team.mephi.adminbot.vaadin.analytics.tabs.AnalyticsTabType.PREORDERS;

/**
 * Провайдер вкладки "Предзаказы" в разделе аналитики.
 */
@SpringComponent
public class PreordersTabProvider implements AnalyticsTabProvider {

    private final CohortService cohortService;

    public PreordersTabProvider(CohortService cohortService) {
        this.cohortService = cohortService;
    }

    @Override
    public AnalyticsTabType getTabId() {
        return PREORDERS;
    }

    @Override
    public String getTabLabel() {
        return PREORDERS.getTabLabelKey();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Component createTabContent(ChartActions<?> actions) {
        return new PreordersView((ChartPresenter<PreordersView.PreorderFilterData>) actions, cohortService);
    }

    @Override
    public Integer getPosition() {
        return PREORDERS.ordinal();
    }
}
