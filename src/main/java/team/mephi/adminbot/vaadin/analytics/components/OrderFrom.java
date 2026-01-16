package team.mephi.adminbot.vaadin.analytics.components;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import lombok.Getter;
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;

/**
 * Форма для выбора параметров заказов на странице аналитики.
 */
public class OrderFrom extends FormLayout {
    @Getter
    private final ComboBox<CohortDto> cohort;
    @Getter
    private final DateRangePicker period;
    @Getter
    private final RadioButtonGroup<ActivityIntervals> interval;
    @Getter
    private final Checkbox detailed;
    @Getter
    private final CheckboxGroup<OrderStatus> statuses;

    /**
     * Создает форму заказов с настройками по умолчанию.
     *
     * @param cohortService Сервис для получения информации о когортах.
     */
    public OrderFrom(CohortService cohortService) {
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

        detailed = new Checkbox();
        detailed.setLabel(getTranslation("page_analytics_form_activity_direction_label"));
        add(detailed);

        statuses = new CheckboxGroup<>();
        statuses.setItems(OrderStatus.values());
        statuses.setItemLabelGenerator(s -> getTranslation(s.getTranslationKey()));
        statuses.select(OrderStatus.values()); // отмечаем все по умолчанию
        statuses.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        addFormItem(statuses, getTranslation("page_analytics_form_activity_status_label"));
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
