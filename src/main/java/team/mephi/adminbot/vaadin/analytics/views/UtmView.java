package team.mephi.adminbot.vaadin.analytics.views;

import lombok.Data;
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.components.UtmForm;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;
import java.util.Objects;

public class UtmView extends AbstractChartView<UtmView.UtmFilterData> {

    public UtmView(ChartPresenter<UtmFilterData> presenter, CohortService cohortService) {
        super(UtmFilterData.class);

        UtmForm form = new UtmForm(cohortService);

        // Биндинг полей — остаётся в дочернем классе, т.к. формы разные
        binder.forField(form.getCohort())
                .withConverter(CohortDto::getId, cohort -> cohortService.getById(cohort).orElse(cohortService.getAllCohorts().getFirst()))
                .bind(UtmFilterData::getCohort, UtmFilterData::setCohort);
        binder.forField(form.getInterval()).bind(UtmFilterData::getInterval, UtmFilterData::setInterval);
        binder.forField(form.getPeriod()).bind(
                p -> new DateRangePicker.LocalDateRange(p.start, p.end),
                (p, v) -> {
                    if (Objects.nonNull(v)) {
                        p.setStart(v.getStartDate());
                        p.setEnd(v.getEndDate());
                    }
                });
        binder.addValueChangeListener(e -> {
            var s = new UtmFilterData();
            binder.writeBeanIfValid(s);
            presenter.onUpdateFilter(s);
        });

        initView(form, presenter, new UtmFilterData());
    }

    @Data
    public static class UtmFilterData {
        private String cohort;
        private LocalDate start;
        private LocalDate end;
        private ActivityIntervals interval;
    }
}
