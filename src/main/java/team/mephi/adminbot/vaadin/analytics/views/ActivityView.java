package team.mephi.adminbot.vaadin.analytics.views;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataChangeEvent;
import lombok.Data;
import software.xdev.chartjs.model.charts.BarChart;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.options.BarOptions;
import software.xdev.chartjs.model.options.LegendOptions;
import software.xdev.vaadin.chartjs.ChartContainer;
import team.mephi.adminbot.vaadin.analytics.components.ActivityForm;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;
import java.util.Objects;

public class ActivityView extends VerticalLayout {
    private final BeanValidationBinder<ActivityFilterData> binder = new BeanValidationBinder<>(ActivityFilterData.class);

    private final ChartContainer chart = new ChartContainer();

    public ActivityView(ChartPresenter<ActivityFilterData> presenter) {
        setPadding(false);

        ActivityForm form = new ActivityForm();
        binder.forField(form.getType()).bind(ActivityFilterData::getType, ActivityFilterData::setType);
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
            var s = new ActivityFilterData();
            binder.writeBeanIfValid(s);
            presenter.onUpdateFilter(s);
        });

        presenter.getDataProvider().addDataProviderListener(event -> {
            if (event instanceof DataChangeEvent.DataRefreshEvent) {
                var data = ((DataChangeEvent.DataRefreshEvent<BarData>) event).getItem();
                updateChart(data);
            }
        });

        VerticalLayout column = new VerticalLayout();
        column.setPadding(false);
        column.setWidth("640px");
        column.add(form);

        HorizontalLayout content = new HorizontalLayout();
        content.setWidthFull();
        content.add(chart, column);

        add(content);

        var buttonGroup = new HorizontalLayout(new SecondaryButton(getTranslation("page_analytics_form_activity_download_png_action"), VaadinIcon.DOWNLOAD_ALT.create()), new SecondaryButton(getTranslation("page_analytics_form_activity_download_excel_action"), VaadinIcon.DOWNLOAD_ALT.create()));
        add(buttonGroup);

        presenter.onUpdateFilter(new ActivityFilterData());
    }

    private void updateChart(BarData data) {
        BarOptions options = new BarOptions();
        options.getPlugins().setLegend(new LegendOptions().setAlign("start").setPosition("bottom"));

        chart.showChart(new BarChart(data, options).toJson());
    }

    @Data
    public static class ActivityFilterData {
        private String type;
        private LocalDate start;
        private LocalDate end;
        private String interval;
    }
}
