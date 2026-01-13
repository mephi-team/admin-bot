package team.mephi.adminbot.vaadin.components.fields;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class DateRangePicker extends CustomField<DateRangePicker.LocalDateRange> {

    private DatePicker start;
    private DatePicker end;

    public DateRangePicker(String label) {
        this();
        setLabel(label);
    }

    public DateRangePicker() {
        start = new DatePicker();
        end = new DatePicker();

        start.setManualValidation(true);
        end.setManualValidation(true);

        add(start, new Text(" – "), end);
    }

    @Override
    protected LocalDateRange generateModelValue() {
        return new LocalDateRange(start.getValue(), end.getValue());
    }

    @Override
    protected void setPresentationValue(LocalDateRange dateRange) {
        start.setValue(dateRange.getStartDate());
        end.setValue(dateRange.getEndDate());
    }

    @Override
    public void setInvalid(boolean invalid) {
        super.setInvalid(invalid);
        start.setInvalid(invalid);
        end.setInvalid(invalid);
    }

    public static class LocalDateRange{
        @Getter
        @Setter
        private LocalDate startDate;
        @Getter
        @Setter
        private LocalDate endDate;
        LocalDateRange(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }
}
