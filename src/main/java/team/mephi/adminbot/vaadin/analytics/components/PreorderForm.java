package team.mephi.adminbot.vaadin.analytics.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import lombok.Getter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;

public class PreorderForm extends FormLayout {
    @Getter
    private final DateRangePicker period;
    @Getter
    private final RadioButtonGroup<ActivityIntervals> interval;
    @Getter
    private final ComboBox<String> cohort;

    public PreorderForm() {
        setAutoResponsive(true);
        setExpandColumns(true);
        setExpandFields(true);

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

        cohort = new ComboBox<>();
        cohort.setItems("Весенний набор 2025");
        cohort.setValue("Весенний набор 2025");
        addFormItem(cohort, getTranslation("page_analytics_form_activity_cohort_label"));
    }
}
