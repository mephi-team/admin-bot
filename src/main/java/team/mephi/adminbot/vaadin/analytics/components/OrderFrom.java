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
        addFormItem(cohort, "Набор");

        period = new DateRangePicker();
        period.setValue(new DateRangePicker.LocalDateRange(
                LocalDate.now(),
                LocalDate.now().plusWeeks(1)
        ));
        addFormItem(period, "Период активности");

        interval = new RadioButtonGroup<>();
        interval.setItems(ActivityIntervals.values());
        interval.setValue(ActivityIntervals.MONTH);
        interval.setItemLabelGenerator(l -> getTranslation(l.getTabLabelKey()));
        addFormItem(interval, "Интервал времени");

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
