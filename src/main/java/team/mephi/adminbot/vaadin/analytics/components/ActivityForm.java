package team.mephi.adminbot.vaadin.analytics.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import lombok.Getter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

public class ActivityForm extends FormLayout {
    @Getter
    private final ComboBox<String> type;
    @Getter
    private final RadioButtonGroup<ActivityIntervals> interval;
    @Getter
    private final DateRangePicker period;

    public ActivityForm() {
        setAutoResponsive(true);
        setExpandColumns(true);
        setExpandFields(true);

        type = new ComboBox<>();
        type.setItems("Посещения", "Популярные кнопки");
        addFormItem(type, getTranslation("page_analytics_form1_type_label"));

        period = new DateRangePicker();
        addFormItem(period, getTranslation("page_analytics_form1_period_label"));

        interval = new RadioButtonGroup<>();
        interval.setItems(ActivityIntervals.values());
        interval.setValue(ActivityIntervals.MONTH);
        interval.setItemLabelGenerator(l -> getTranslation(l.getTabLabelKey()));
        interval.addValueChangeListener(e -> {
            changeDatePicker(e.getValue());
        });
        addFormItem(interval, getTranslation("page_analytics_form1_interval_label"));
    }
    private void changeDatePicker(ActivityIntervals interval) {
        period.changeMode(interval == ActivityIntervals.HOUR ? DateRangePicker.Mode.DAY : DateRangePicker.Mode.INTERVAL);
    }
}
