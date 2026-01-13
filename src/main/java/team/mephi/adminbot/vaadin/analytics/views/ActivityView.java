package team.mephi.adminbot.vaadin.analytics.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import software.xdev.vaadin.chartjs.ChartContainer;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;

public class ActivityView extends VerticalLayout {
    public ActivityView() {
        setPadding(false);
        ChartContainer chart = new ChartContainer();
        chart.showChart(
                "{\"data\":{\"labels\":[\"A\",\"B\"],\"datasets\":[{\"data\":[1,2],\"label\":\"X\"}]},\"type\":\"bar\"}");

        FormLayout form = new FormLayout();
        form.setAutoResponsive(true);
        form.setExpandColumns(true);
        form.setExpandFields(true);

        var list = new ComboBox<String>();
        list.setItems("Test");
        form.addFormItem(list, "Тип активности");
        form.addFormItem(new DatePicker(), "Период активности");
        var group = new RadioButtonGroup<String>();
        group.setItems("1 месяц", "1 день", "1 час");
        group.setValue("1 месяц");
        form.addFormItem(group, "Интервал времени");

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
