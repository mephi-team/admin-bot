package team.mephi.adminbot.vaadin.analytics.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import software.xdev.chartjs.model.charts.BarChart;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.dataset.BarDataset;
import software.xdev.chartjs.model.options.BarOptions;
import software.xdev.chartjs.model.options.LegendOptions;
import software.xdev.vaadin.chartjs.ChartContainer;
import team.mephi.adminbot.vaadin.analytics.components.Form1;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;

import java.util.List;

public class ActivityView extends VerticalLayout {
    private final ChartContainer chart = new ChartContainer();
    private List<String> labels = List.of("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");

    public ActivityView() {
        setPadding(false);

        // начальная заглушка (пустой график)
        updateChart(List.of(0,0,0,0,0,0,0,0,0,0,0,0));

        FormLayout form = new Form1();
//        form.setAutoResponsive(true);
//        form.setExpandColumns(true);
//        form.setExpandFields(true);
//
//        var list = new ComboBox<String>();
//        list.setItems("Test");
//        form.addFormItem(list, "Тип активности");
//        form.addFormItem(new DatePicker(), "Период активности");
//        var group = new RadioButtonGroup<String>();
//        group.setItems("1 месяц", "1 день", "1 час");
//        group.setValue("1 месяц");
//        group.addValueChangeListener(e -> {
//            var value = e.getValue();
//            if ("1 месяц".equals(value)) {
//                labels = List.of("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
//            } else if ("1 день".equals(value)) {
//                labels = List.of("12.01.2026", "13.01.2026", "14.01.2026", "15.01.2026", "16.01.2026", "17.01.2026", "18.01.2026", "19.01.2026", "20.01.2026", "21.01.2026", "22.01.2026", "23.01.2026");
//            } else if ("1 час".equals(value)) {
//                labels = List.of("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
//            }
//            Random random = new Random();
//            updateChart(labels.stream().map(s ->random.nextInt(0, 100)).toList());
//        });
//        form.addFormItem(group, "Интервал времени");

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
}
