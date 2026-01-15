package team.mephi.adminbot.vaadin.analytics.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartActions;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.analytics.views.OrdersView;

import static team.mephi.adminbot.vaadin.analytics.tabs.AnalyticsTabType.ORDERS;

/**
 * Провайдер вкладки "Заказы" в разделе аналитики.
 */
@SpringComponent
public class OrdersTabProvider implements AnalyticsTabProvider {

    private final CohortService cohortService;

    /**
     * Конструктор провайдера вкладки "Заказы".
     *
     * @param cohortService сервис для работы с когортами пользователей
     */
    public OrdersTabProvider(CohortService cohortService) {
        this.cohortService = cohortService;
    }

    @Override
    public AnalyticsTabType getTabId() {
        return ORDERS;
    }

    @Override
    public String getTabLabel() {
        return ORDERS.getTabLabelKey();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Component createTabContent(ChartActions<?> actions) {
        return new OrdersView((ChartPresenter<OrdersView.OrderFilterData>) actions, cohortService);
    }

    @Override
    public Integer getPosition() {
        return ORDERS.ordinal();
    }
}
