package team.mephi.adminbot.vaadin.analytics.views;

import lombok.Data;
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.components.PreorderForm;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;
import java.util.Objects;

public class PreordersView extends AbstractChartView<PreordersView.PreorderFilterData> {

    public PreordersView(ChartPresenter<PreorderFilterData> presenter, CohortService cohortService) {
        super(PreorderFilterData.class);

        PreorderForm form = new PreorderForm(cohortService);

        // Биндинг полей — остаётся в дочернем классе, т.к. формы разные
        binder.forField(form.getCohort())
                .withConverter(CohortDto::getId, cohort -> cohortService.getById(cohort).orElse(cohortService.getAllCohorts().getFirst()))
                .bind(PreorderFilterData::getCohort, PreorderFilterData::setCohort);
        binder.forField(form.getInterval()).bind(PreorderFilterData::getInterval, PreorderFilterData::setInterval);
        binder.forField(form.getPeriod()).bind(
                p -> new DateRangePicker.LocalDateRange(p.start, p.end),
                (p, v) -> {
                    if (Objects.nonNull(v)) {
                        p.setStart(v.getStartDate());
                        p.setEnd(v.getEndDate());
                    }
                });
        binder.addValueChangeListener(e -> {
            var s = new PreorderFilterData();
            binder.writeBeanIfValid(s);
            presenter.onUpdateFilter(s);
        });

        initView(form, presenter, new PreorderFilterData());
    }

    @Data
    public static class PreorderFilterData {
        private String cohort;
        private LocalDate start = LocalDate.now();
        private LocalDate end = LocalDate.now().plusWeeks(1);
        private ActivityIntervals interval;
    }
}
