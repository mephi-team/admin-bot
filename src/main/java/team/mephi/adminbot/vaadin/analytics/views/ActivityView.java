package team.mephi.adminbot.vaadin.analytics.views;

import lombok.Data;
import team.mephi.adminbot.vaadin.analytics.components.ActivityForm;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;
import java.util.Objects;

public class ActivityView extends AbstractChartView<ActivityView.ActivityFilterData> {

    public ActivityView(ChartPresenter<ActivityFilterData> presenter) {
        super(ActivityFilterData.class);

        ActivityForm form = new ActivityForm();

        // Биндинг полей — остаётся в дочернем классе, т.к. формы разные
        binder.forField(form.getType()).bind(ActivityFilterData::getType, ActivityFilterData::setType);
        binder.forField(form.getInterval()).bind(
                s -> Objects.isNull(s.interval) ? null : ActivityIntervals.valueOf(s.interval),
                (s, v) -> s.setInterval(v.toString())
        );
        binder.forField(form.getPeriod()).bind(
                p -> new DateRangePicker.LocalDateRange(p.start, p.end),
                (p, v) -> {
                    if (Objects.nonNull(v)) {
                        p.setStart(v.getStartDate());
                        p.setEnd(v.getEndDate());
                    }
                }
        );

        // Общая реакция на изменение формы
        binder.addValueChangeListener(e -> {
            var s = new ActivityFilterData();
            binder.writeBeanIfValid(s);
            presenter.onUpdateFilter(s);
        });

        initView(form, presenter, new ActivityFilterData());
    }

    @Data
    public static class ActivityFilterData {
        private String type;
        private LocalDate start;
        private LocalDate end;
        private String interval;
    }
}