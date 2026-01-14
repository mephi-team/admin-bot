package team.mephi.adminbot.vaadin.analytics.views;

import lombok.Data;
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.components.OrderFrom;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;
import java.util.Objects;

public class OrdersView extends AbstractChartView<OrdersView.OrderFilterData> {

    public OrdersView(ChartPresenter<OrderFilterData> presenter, CohortService cohortService) {
        super(OrderFilterData.class);

        OrderFrom form = new OrderFrom(cohortService);

        // Биндинг полей — остаётся в дочернем классе, т.к. формы разные
        binder.forField(form.getCohort())
                .withConverter(CohortDto::getName, cohort -> cohortService.getByName(cohort).orElse(cohortService.getAllCohorts().getFirst()))
                .bind(OrderFilterData::getCohort, OrderFilterData::setCohort);
        binder.forField(form.getInterval()).bind(s -> Objects.isNull(s.interval) ? null : ActivityIntervals.valueOf(s.interval), (s, v) -> s.setInterval(v.toString()));
        binder.forField(form.getPeriod()).bind(
                p -> new DateRangePicker.LocalDateRange(p.start, p.end),
                (p, v) -> {
                    if (Objects.nonNull(v)) {
                        p.setStart(v.getStartDate());
                        p.setEnd(v.getEndDate());
                    }
                });
        binder.addValueChangeListener(e -> {
            var s = new OrderFilterData();
            binder.writeBeanIfValid(s);
            presenter.onUpdateFilter(s);
        });

        initView(form, presenter, new OrderFilterData());
    }

    @Data
    public static class OrderFilterData {
        private String cohort;
        private LocalDate start;
        private LocalDate end;
        private String interval;
    }
}
