package team.mephi.adminbot.vaadin.analytics.views;

import lombok.Data;
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.components.UtmForm;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Представление для отображения UTM-меток.
 */
public class UtmView extends AbstractChartView<UtmView.UtmFilterData> {

    /**
     * Конструктор представления UTM-меток.
     *
     * @param presenter     объект ChartPresenter, отвечающий за обработку данных фильтра и обновление графика.
     * @param cohortService сервис для работы с когортами.
     */
    public UtmView(ChartPresenter<UtmFilterData> presenter, CohortService cohortService) {
        super(UtmFilterData.class);

        // Создание формы для фильтрации UTM-меток.
        UtmForm form = new UtmForm(cohortService);

        // Привязка полей формы к данным фильтра.
        binder.forField(form.getCohort())
                .withConverter(CohortDto::getId, cohort -> cohortService.getById(cohort).orElse(cohortService.getAllCohorts().getFirst()))
                .bind(UtmFilterData::getCohort, UtmFilterData::setCohort);
        binder.forField(form.getInterval()).bind(UtmFilterData::getInterval, UtmFilterData::setInterval);
        binder.forField(form.getPeriod()).bind(
                p -> new DateRangePicker.LocalDateRange(p.start, p.end),
                (p, v) -> {
                    if (Objects.nonNull(v)) {
                        p.setStart(v.getStartDate());
                        p.setEnd(v.getEndDate());
                    }
                });

        // Добавление слушателя изменений значений в форме.
        binder.addValueChangeListener(ignoredEvent -> {
            var s = new UtmFilterData();
            binder.writeBeanIfValid(s);
            presenter.onUpdateFilter(s);
        });

        // Инициализация представления с формой, презентером и начальными данными фильтра.
        initView(form, presenter, new UtmFilterData());
    }

    /**
     * Вложенный класс, представляющий данные фильтра для UTM-меток.
     */
    @Data
    public static class UtmFilterData {
        private String cohort;
        private LocalDate start;
        private LocalDate end;
        private ActivityIntervals interval;
    }
}
