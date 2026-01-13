package team.mephi.adminbot.vaadin.components.fields;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

    public enum Mode {
        DAY,
        INTERVAL
    }
}
