package team.mephi.adminbot.vaadin.analytics.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import software.xdev.chartjs.model.charts.BarChart;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.dataset.BarDataset;
import software.xdev.chartjs.model.options.BarOptions;
import software.xdev.chartjs.model.options.LegendOptions;
import software.xdev.vaadin.chartjs.ChartContainer;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

public class UtmView extends VerticalLayout {
    public UtmView() {
        setPadding(false);

        BarData barData = new BarData().addLabels("Январь", "Февраль", "Март");
        barData.addDataset(new BarDataset().setLabel("Test1").setBackgroundColor("#2168df").addData(5).addData(10).addData(8));

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

        VerticalLayout column = new VerticalLayout();
        column.setPadding(false);
        column.setWidth("640px");
        column.add(form);

        TabSheet tabSheet = new TabSheet();
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_MINIMAL);
        tabSheet.add("По источнику",new UtmFilter1());
        tabSheet.add("По способу доставки", new UtmFilter2());
        form.add(tabSheet);

        HorizontalLayout content = new HorizontalLayout();
        content.setWidthFull();
        content.add(chart, column);

        add(content);

        var buttonGroup = new HorizontalLayout(new SecondaryButton("Скачать PNG", VaadinIcon.DOWNLOAD_ALT.create()), new SecondaryButton("Скачать Excel", VaadinIcon.DOWNLOAD_ALT.create()));
        add(buttonGroup);
    }
}
