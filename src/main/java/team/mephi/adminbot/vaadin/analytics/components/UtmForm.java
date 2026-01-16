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

/**
 * Форма для выбора параметров UTM на странице аналитики.
 */
public class UtmForm extends FormLayout {
    @Getter
    private final ComboBox<CohortDto> cohort;
    @Getter
    private final DateRangePicker period;
    @Getter
    private final RadioButtonGroup<ActivityIntervals> interval;

    /**
     * Создает форму UTM с настройками по умолчанию.
     *
     * @param cohortService Сервис для получения информации о когортах.
     */
    public UtmForm(CohortService cohortService) {
        setAutoResponsive(true);
        setExpandColumns(true);
        setExpandFields(true);

        cohort = new ComboBox<>();
        cohort.setItemsPageable(cohortService::getAllCohorts);
        cohort.setItemLabelGenerator(CohortDto::getDisplayName);
        cohort.setValue(cohortService.getDefaultCohort());
        addFormItem(cohort, getTranslation("page_analytics_form_activity_cohort_label"));

        period = new DateRangePicker();
        period.setValue(new DateRangePicker.LocalDateRange(
                LocalDate.now(),
                LocalDate.now().plusWeeks(1)
        ));
        addFormItem(period, getTranslation("page_analytics_form_activity_period_label"));

        interval = new RadioButtonGroup<>();
        interval.addThemeName("chip");
        interval.setItems(ActivityIntervals.values());
        interval.setValue(ActivityIntervals.MONTH);
        interval.setItemLabelGenerator(l -> getTranslation(l.getTabLabelKey()));
        interval.addValueChangeListener(e -> changeDatePicker(e.getValue()));
        addFormItem(interval, getTranslation("page_analytics_form_activity_interval_label"));

        TabSheet tabSheet = new TabSheet();
        tabSheet.addThemeNames(TabSheetVariant.LUMO_TABS_EQUAL_WIDTH_TABS.getVariantName(), "button-group");
        tabSheet.add(getTranslation("page_analytics_form_activity_tabs_source_label"), new UtmFilterSource());
        tabSheet.add(getTranslation("page_analytics_form_activity_tabs_delivery_label"), new UtmFilterDelivery());
        add(tabSheet);
    }

    /**
     * Изменяет режим выбора даты в зависимости от выбранного интервала.
     *
     * @param interval Выбранный интервал активности.
     */
    private void changeDatePicker(ActivityIntervals interval) {
        period.changeMode(interval == ActivityIntervals.HOUR ? DateRangePicker.Mode.DAY : DateRangePicker.Mode.INTERVAL);
    }
}
