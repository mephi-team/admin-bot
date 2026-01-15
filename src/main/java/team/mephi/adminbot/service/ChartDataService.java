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
    /**
     * Получает данные диаграммы для активности на основе предоставленных фильтров.
     *
     * @param data данные фильтрации активности
     * @return данные диаграммы в формате BarData
     */
    BarData forActivity(ActivityView.ActivityFilterData data);

    /**
     * Получает данные диаграммы для предзаказов на основе предоставленных фильтров.
     *
     * @param data данные фильтрации предзаказов
     * @return данные диаграммы в формате BarData
     */
    BarData forPreorders(PreordersView.PreorderFilterData data);

    /**
     * Получает данные диаграммы для заказов на основе предоставленных фильтров.
     *
     * @param data данные фильтрации заказов
     * @return данные диаграммы в формате BarData
     */
    BarData forOrders(OrdersView.OrderFilterData data);

    /**
     * Получает данные диаграммы для UTM-меток на основе предоставленных фильтров.
     *
     * @param data данные фильтрации UTM-меток
     * @return данные диаграммы в формате BarData
     */
    BarData forUtm(UtmView.UtmFilterData data);
}
