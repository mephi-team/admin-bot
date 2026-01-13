package team.mephi.adminbot.vaadin.analytics.views;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import lombok.Data;
import software.xdev.chartjs.model.charts.BarChart;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.dataset.BarDataset;
import software.xdev.chartjs.model.options.BarOptions;
import software.xdev.chartjs.model.options.LegendOptions;
import software.xdev.vaadin.chartjs.ChartContainer;
import team.mephi.adminbot.vaadin.analytics.components.ActivityForm;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ActivityView extends VerticalLayout {
    private final BeanValidationBinder<SimpleData> binder = new BeanValidationBinder<>(SimpleData.class);

    private final ChartContainer chart = new ChartContainer();
    private List<String> labels = List.of("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");

    Random random = new Random();

    public ActivityView() {
        setPadding(false);

        // начальная заглушка (пустой график)
        updateChart(labels.stream().map(s1 -> random.nextInt(0, 100)).toList());

        ActivityForm form = new ActivityForm();
        binder.forField(form.getType()).bind(SimpleData::getType, SimpleData::setType);
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
            var s = new SimpleData();
            binder.writeBeanIfValid(s);
            System.out.println("Form changed: " + s);
            if (ActivityIntervals.MONTH.name().equals(s.interval)) {
                labels = List.of("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
            } else if (ActivityIntervals.DAY.name().equals(s.interval)) {
                labels = List.of("12.01.2026", "13.01.2026", "14.01.2026", "15.01.2026", "16.01.2026", "17.01.2026", "18.01.2026", "19.01.2026", "20.01.2026", "21.01.2026", "22.01.2026", "23.01.2026");
            } else if (ActivityIntervals.HOUR.name().equals(s.interval)) {
                labels = List.of("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
            }
            updateChart(labels.stream().map(s1 -> random.nextInt(0, 100)).toList());
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
    }

    private void updateChart(List<Integer> values) {
        // ожидается 12 значений; при необходимости нормализовать/заполнить
        if (values == null || values.size() < labels.size()) {
            values = labels.stream().map(s -> 0).toList();
        }

        BarData barData = new BarData()
                .addLabels(labels.toArray(new String[0]));
        BarDataset dataset = new BarDataset().setLabel("Активность").setBackgroundColor("#2168df");
        values.forEach(dataset::addData);
        barData.addDataset(dataset);

        BarOptions options = new BarOptions();
        options.getPlugins().setLegend(new LegendOptions().setAlign("start").setPosition("bottom"));

        chart.showChart(new BarChart(barData, options).toJson());
    }

    @Data
    public static class SimpleData {
        private String type;
        private LocalDate start;
        private LocalDate end;
        private String interval;
    }
}
