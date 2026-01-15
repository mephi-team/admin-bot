package team.mephi.adminbot.service;

import software.xdev.chartjs.model.data.BarData;
import team.mephi.adminbot.vaadin.analytics.views.ActivityView;
import team.mephi.adminbot.vaadin.analytics.views.OrdersView;
import team.mephi.adminbot.vaadin.analytics.views.PreordersView;
import team.mephi.adminbot.vaadin.analytics.views.UtmView;

/**
 * Сервис для получения данных диаграмм по различным фильтрам.
 */
public interface ChartDataService {
    BarData forActivity(ActivityView.ActivityFilterData data);

    BarData forPreorders(PreordersView.PreorderFilterData data);

    BarData forOrders(OrdersView.OrderFilterData data);

    BarData forUtm(UtmView.UtmFilterData data);
}
