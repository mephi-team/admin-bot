package team.mephi.adminbot.vaadin.analytics.components;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import lombok.Getter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;

public class OrderFrom extends FormLayout {
    @Getter
    private final ComboBox<String> cohort;
    @Getter
    private final DateRangePicker period;
    @Getter
    private final RadioButtonGroup<ActivityIntervals> interval;

    public OrderFrom() {
        setAutoResponsive(true);
        setExpandColumns(true);
        setExpandFields(true);

        cohort = new ComboBox<>();
        cohort.setItems("Весенний набор 2025");
        cohort.setValue("Весенний набор 2025");
        addFormItem(cohort, getTranslation("page_analytics_form_activity_cohort_label"));

        period = new DateRangePicker();
        period.setValue(new DateRangePicker.LocalDateRange(
                LocalDate.now(),
                LocalDate.now().plusWeeks(1)
        ));
        addFormItem(period, getTranslation("page_analytics_form_activity_period_label"));

        interval = new RadioButtonGroup<>();
        interval.setItems(ActivityIntervals.values());
        interval.setValue(ActivityIntervals.MONTH);
        interval.setItemLabelGenerator(l -> getTranslation(l.getTabLabelKey()));
        addFormItem(interval, getTranslation("page_analytics_form_activity_interval_label"));

        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("Детализировать по направлениям");
        add(checkbox);

        CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
        checkboxGroup.setItems("Все заявки", "Актуальные заявки", "Отозванные заявки");
        checkboxGroup.select("Все заявки", "Актуальные заявки", "Отозванные заявки");
        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        addFormItem(checkboxGroup, "Статус заявки");
    }
}
