package team.mephi.adminbot.vaadin.components.fields;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Arrays;

public class DateRangePicker extends CustomField<DateRangePicker.LocalDateRange> {

    private DatePicker start;
    private DatePicker end;

    private Mode mode = Mode.INTERVAL;
    private HorizontalLayout container;

    public DateRangePicker(String label) {
        this();
        setLabel(label);
    }

    public DateRangePicker() {
        start = new DatePicker();
        end = new DatePicker();

        setI18n(createRussianI18n());

        start.setManualValidation(true);
        end.setManualValidation(true);

        container = new HorizontalLayout();
        container.setWidthFull();
        container.setAlignItems(FlexComponent.Alignment.CENTER);

        start.setWidthFull();
        container.add(start, end);

        container.setFlexGrow(1.0, start);

        add(container);
    }

    /**
     * Устанавливает i18n для обоих DatePicker'ов.
     */
    public void setI18n(DatePicker.DatePickerI18n i18n) {
        start.setI18n(i18n);
        end.setI18n(i18n);
    }

    private DatePicker.DatePickerI18n createRussianI18n() {
        return new DatePicker.DatePickerI18n()
                .setMonthNames(Arrays.asList(
                        getTranslation("datepicker_months_january"),
                        getTranslation("datepicker_months_february"),
                        getTranslation("datepicker_months_march"),
                        getTranslation("datepicker_months_april"),
                        getTranslation("datepicker_months_may"),
                        getTranslation("datepicker_months_june"),
                        getTranslation("datepicker_months_july"),
                        getTranslation("datepicker_months_august"),
                        getTranslation("datepicker_months_september"),
                        getTranslation("datepicker_months_october"),
                        getTranslation("datepicker_months_november"),
                        getTranslation("datepicker_months_december")))
                .setWeekdays(Arrays.asList(
                        getTranslation("datepicker_weekdays_sunday"),
                        getTranslation("datepicker_weekdays_monday"),
                        getTranslation("datepicker_weekdays_tuesday"),
                        getTranslation("datepicker_weekdays_wednesday"),
                        getTranslation("datepicker_weekdays_thursday"),
                        getTranslation("datepicker_weekdays_friday"),
                        getTranslation("datepicker_weekdays_saturday")))
                .setWeekdaysShort(Arrays.asList(
                        getTranslation("datepicker_weekdays_sun"),
                        getTranslation("datepicker_weekdays_mon"),
                        getTranslation("datepicker_weekdays_tue"),
                        getTranslation("datepicker_weekdays_wed"),
                        getTranslation("datepicker_weekdays_thu"),
                        getTranslation("datepicker_weekdays_fri"),
                        getTranslation("datepicker_weekdays_sat")))
                .setToday(getTranslation("datepicker_today"))
                .setCancel(getTranslation("datepicker_cancel"))
                .setFirstDayOfWeek(1);
    }

    public void changeMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.DAY) {
            end.setVisible(false);
            // гарантировать, что start занимает всё пространство
            start.setWidthFull();
            container.setFlexGrow(1.0, start);
        } else {
            end.setVisible(true);
            // вернуть стандартное поведение (start всё ещё может быть гибким)
            start.setWidthFull();
            container.setFlexGrow(1.0, start);
        }
    }

    @Override
    protected LocalDateRange generateModelValue() {
        return new LocalDateRange(start.getValue(), end.getValue());
    }

    @Override
    protected void setPresentationValue(LocalDateRange dateRange) {
        if (dateRange == null) {
            start.clear();
            end.clear();
            return;
        }
        start.setValue(dateRange.getStartDate());
        end.setValue(dateRange.getEndDate());
    }

    @Override
    public void setInvalid(boolean invalid) {
        super.setInvalid(invalid);
        start.setInvalid(invalid);
        end.setInvalid(invalid);
    }

    public enum Mode {
        DAY,
        INTERVAL
    }

    public static class LocalDateRange {
        @Getter
        @Setter
        private LocalDate startDate;
        @Getter
        @Setter
        private LocalDate endDate;

        public LocalDateRange(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }
}
