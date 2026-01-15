package team.mephi.adminbot.vaadin.analytics.views;

import lombok.Data;
import team.mephi.adminbot.vaadin.analytics.components.ActivityForm;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.components.ActivityType;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;
import java.util.Objects;

public class ActivityView extends AbstractChartView<ActivityView.ActivityFilterData> {

    public ActivityView(ChartPresenter<ActivityFilterData> presenter) {
        super(ActivityFilterData.class);

        ActivityForm form = new ActivityForm();

        binder.forField(form.getType()).bind(ActivityFilterData::getType, ActivityFilterData::setType);
        binder.forField(form.getInterval()).bind(ActivityFilterData::getInterval, ActivityFilterData::setInterval);
        binder.forField(form.getPeriod()).bind(
                p -> new DateRangePicker.LocalDateRange(p.start, p.end),
                (p, v) -> {
                    if (Objects.nonNull(v)) {
                        p.setStart(v.getStartDate());
                        p.setEnd(v.getEndDate());
                    }
                }
        );
        binder.addValueChangeListener(ignoredEvent -> {
            var s = new ActivityFilterData();
            binder.writeBeanIfValid(s);
            presenter.onUpdateFilter(s);
        });

        initView(form, presenter, new ActivityFilterData());
    }

    @Data
    public static class ActivityFilterData {
        private ActivityType type;
        private LocalDate start = LocalDate.now();
        private LocalDate end = LocalDate.now().plusWeeks(1);
        private ActivityIntervals interval;
    }
}