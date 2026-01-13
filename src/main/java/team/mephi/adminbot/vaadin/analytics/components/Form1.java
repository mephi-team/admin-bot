package team.mephi.adminbot.vaadin.analytics.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import lombok.Getter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

public class Form1 extends FormLayout {
    @Getter
    private final ComboBox<String> type;
    @Getter
    private final RadioButtonGroup<Form1Intervals> interval;
    @Getter
    private final DateRangePicker period;

    public Form1() {
        setAutoResponsive(true);
        setExpandColumns(true);
        setExpandFields(true);

        type = new ComboBox<>();
        type.setItems("Посещения", "Популярные кнопки");
        addFormItem(type, "Тип активности");

        period = new DateRangePicker();
        addFormItem(period, "Период активности");

        interval = new RadioButtonGroup<>();
        interval.setItems(Form1Intervals.values());
        interval.setValue(Form1Intervals.MONTH);
        interval.addValueChangeListener(e -> {
            changeDatePicker(e.getValue());
        });
        addFormItem(interval, "Интервал времени");
    }
    private void changeDatePicker(Form1Intervals interval) {
        period.changeMode(interval == Form1Intervals.HOUR ? DateRangePicker.Mode.DAY : DateRangePicker.Mode.INTERVAL);
    }
}
