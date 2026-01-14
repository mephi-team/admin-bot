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
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.components.OrderFrom;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;
import java.util.Objects;

public class OrdersView extends VerticalLayout {
    private final BeanValidationBinder<OrderFilterData> binder = new BeanValidationBinder<>(OrderFilterData.class);

    private final ChartContainer chart = new ChartContainer();

    public OrdersView(ChartPresenter<OrderFilterData> presenter, CohortService cohortService) {
        setPadding(false);

        OrderFrom form = new OrderFrom(cohortService);
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

        var buttonGroup = new HorizontalLayout(new SecondaryButton("Скачать PNG", VaadinIcon.DOWNLOAD_ALT.create()), new SecondaryButton("Скачать Excel", VaadinIcon.DOWNLOAD_ALT.create()));
        add(buttonGroup);

        presenter.onUpdateFilter(new OrderFilterData());
    }

    private void updateChart(BarData data) {
        BarOptions options = new BarOptions();
        options.getPlugins().setLegend(new LegendOptions().setAlign("start").setPosition("bottom"));

        chart.showChart(new BarChart(data, options).toJson());
    }

    @Data
    public static class OrderFilterData {
        private String cohort;
        private LocalDate start;
        private LocalDate end;
        private String interval;
    }
}
