package team.mephi.adminbot.vaadin.analytics.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import software.xdev.chartjs.model.charts.BarChart;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.dataset.BarDataset;
import software.xdev.chartjs.model.options.BarOptions;
import software.xdev.chartjs.model.options.LegendOptions;
import software.xdev.vaadin.chartjs.ChartContainer;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

public class OrdersView extends VerticalLayout {
    public OrdersView() {
        setPadding(false);

        BarData barData = new BarData().addLabels("Январь", "Февраль", "Март");
        barData.addDataset(new BarDataset().setLabel("Test1").setBackgroundColor("#2168df").addData(10).addData(15).addData(12));
        barData.addDataset(new BarDataset().setLabel("Test2").setBackgroundColor("#d3e1f9").addData(2).addData(4).addData(1));
        barData.addDataset(new BarDataset().setLabel("Test3").addData(13).addData(14).addData(15));

        BarOptions options = new BarOptions();
        options.getPlugins().setLegend(new LegendOptions().setPosition("bottom"));

        ChartContainer chart = new ChartContainer();
        chart.showChart(new BarChart(barData, options).toJson());

        FormLayout form = new FormLayout();
        form.setAutoResponsive(true);
        form.setExpandColumns(true);
        form.setExpandFields(true);

        var list = new ComboBox<String>();
        list.setItems("Test");
        form.addFormItem(list, "Набор");

        form.addFormItem(new DateRangePicker(), "Период активности");
        var group = new RadioButtonGroup<String>();
        group.setItems("1 месяц", "1 день", "1 час");
        group.setValue("1 месяц");
        form.addFormItem(group, "Интервал времени");

        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("Детализировать по направлениям");
        form.add(checkbox);

        CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
        checkboxGroup.setItems("Все заявки", "Актуальные заявки", "Отозванные заявки");
        checkboxGroup.select("Все заявки", "Актуальные заявки", "Отозванные заявки");
        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        form.addFormItem(checkboxGroup, "Статус заявки");

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
}
