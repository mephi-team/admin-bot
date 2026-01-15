package team.mephi.adminbot.vaadin.analytics.presenter;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.ChartDataService;
import team.mephi.adminbot.vaadin.analytics.dataproviders.ChartDataProvider;

/**
 * Фабрика для создания презентеров графиков на странице аналитики.
 */
@SpringComponent
public class ChartPresenterFactory {

    private final ChartDataService chartDataService;

    /**
     * Конструктор фабрики презентеров.
     *
     * @param chartDataService сервис для получения данных графиков
     */
    public ChartPresenterFactory(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }

    /**
     * Создает новый презентер для графиков.
     *
     * @return новый экземпляр ChartActions
     */
    public ChartActions<?> createPresenter() {
        return new ChartPresenter<>(new ChartDataProvider<ChartDataService>(chartDataService));
    }
}
