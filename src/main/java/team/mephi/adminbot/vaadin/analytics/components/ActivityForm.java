package team.mephi.adminbot.vaadin.analytics.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import lombok.Getter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;

/**
 * Форма для выбора параметров активности на странице аналитики.
 */
public class ActivityForm extends FormLayout {
    @Getter
    private final ComboBox<ActivityType> type;
    @Getter
    private final RadioButtonGroup<ActivityIntervals> interval;
    @Getter
    private final DateRangePicker period;

    /**
     * Создает форму активности с настройками по умолчанию.
     */
    public ActivityForm() {
        setAutoResponsive(true);
        setExpandColumns(true);
        setExpandFields(true);

        type = new ComboBox<>();
        type.setItems(ActivityType.values());
        type.setItemLabelGenerator(t -> getTranslation(t.getLabelKey()));
        type.setValue(ActivityType.VISITS);
        addFormItem(type, getTranslation("page_analytics_form_activity_type_label"));

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
        interval.addValueChangeListener(e -> changeDatePicker(e.getValue()));
        addFormItem(interval, getTranslation("page_analytics_form_activity_interval_label"));
    }

    /**
     * Изменяет режим выбора даты в зависимости от выбранного интервала.
     * @param interval
     */
    private void changeDatePicker(ActivityIntervals interval) {
        period.changeMode(interval == ActivityIntervals.HOUR ? DateRangePicker.Mode.DAY : DateRangePicker.Mode.INTERVAL);
    }
}
