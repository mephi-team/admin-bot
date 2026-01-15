package team.mephi.adminbot.vaadin.analytics.views;

import lombok.Data;
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.components.OrderFrom;
import team.mephi.adminbot.vaadin.analytics.components.OrderStatus;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * Представление для отображения заказов.
 */
public class OrdersView extends AbstractChartView<OrdersView.OrderFilterData> {

    /**
     * Конструктор представления заказов.
     *
     * @param presenter     объект ChartPresenter, отвечающий за обработку данных фильтра и обновление графика.
     * @param cohortService сервис для работы с когортами.
     */
    public OrdersView(ChartPresenter<OrderFilterData> presenter, CohortService cohortService) {
        super(OrderFilterData.class);

        // Создание формы для фильтрации заказов.
        OrderFrom form = new OrderFrom(cohortService);

        // Привязка полей формы к данным фильтра.
        binder.forField(form.getCohort())
                .withConverter(CohortDto::getId, cohort -> cohortService.getById(cohort).orElse(cohortService.getAllCohorts().getFirst()))
                .bind(OrderFilterData::getCohort, OrderFilterData::setCohort);
        binder.forField(form.getInterval()).bind(OrderFilterData::getInterval, OrderFilterData::setInterval);
        binder.forField(form.getPeriod()).bind(
                p -> new DateRangePicker.LocalDateRange(p.start, p.end),
                (p, v) -> {
                    if (Objects.nonNull(v)) {
                        p.setStart(v.getStartDate());
                        p.setEnd(v.getEndDate());
                    }
                });
        binder.forField(form.getDetailed())
                .bind(OrderFilterData::getDetailed, OrderFilterData::setDetailed);
        binder.forField(form.getStatuses())
                .bind(OrderFilterData::getStatuses, OrderFilterData::setStatuses);

        // Добавление слушателя изменений значений в форме.
        binder.addValueChangeListener(ignoredEvent -> {
            var s = new OrderFilterData();
            binder.writeBeanIfValid(s);
            presenter.onUpdateFilter(s);
        });

        // Инициализация представления с формой, презентером и начальными данными фильтра.
        initView(form, presenter, new OrderFilterData());
    }

    /**
     * Вложенный класс, представляющий данные фильтра для заказов.
     */
    @Data
    public static class OrderFilterData {
        private String cohort;
        private LocalDate start = LocalDate.now();
        private LocalDate end = LocalDate.now().plusWeeks(1);
        private ActivityIntervals interval;
        private Boolean detailed;
        private Set<OrderStatus> statuses = Set.of(OrderStatus.values());
    }
}
