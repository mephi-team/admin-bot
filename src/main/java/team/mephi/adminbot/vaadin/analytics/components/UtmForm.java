package team.mephi.adminbot.vaadin.analytics.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import lombok.Getter;
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;

public class UtmForm extends FormLayout  {
    @Getter
    private final ComboBox<CohortDto> cohort;
    @Getter
    private final DateRangePicker period;
    @Getter
    private final RadioButtonGroup<ActivityIntervals> interval;

    public UtmForm(CohortService cohortService) {
        setAutoResponsive(true);
        setExpandColumns(true);
        setExpandFields(true);

        cohort = new ComboBox<>();
        cohort.setItemsPageable(cohortService::getAllCohorts);
        cohort.setItemLabelGenerator(CohortDto::getName);
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
        interval.addValueChangeListener(e -> {
            changeDatePicker(e.getValue());
        });
        addFormItem(interval, getTranslation("page_analytics_form_activity_interval_label"));

        TabSheet tabSheet = new TabSheet();
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_MINIMAL);
        tabSheet.add(getTranslation("page_analytics_form_activity_tabs_source_label"),new UtmFilterSource());
        tabSheet.add(getTranslation("page_analytics_form_activity_tabs_delivery_label"), new UtmFilterDelivery());
        add(tabSheet);
    }

    private void changeDatePicker(ActivityIntervals interval) {
        period.changeMode(interval == ActivityIntervals.HOUR ? DateRangePicker.Mode.DAY : DateRangePicker.Mode.INTERVAL);
    }
}
