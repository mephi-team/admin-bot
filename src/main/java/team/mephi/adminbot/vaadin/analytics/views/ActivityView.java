package team.mephi.adminbot.vaadin.analytics.views;

import lombok.Data;
import team.mephi.adminbot.vaadin.analytics.components.ActivityForm;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.components.ActivityType;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.components.fields.DateRangePicker;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Представление для отображения активности пользователей.
 */
public class ActivityView extends AbstractChartView<ActivityView.ActivityFilterData> {

    /**
     * Конструктор представления активности.
     *
     * @param presenter объект ChartPresenter, отвечающий за обработку данных фильтра и обновление графика.
     */
    public ActivityView(ChartPresenter<ActivityFilterData> presenter) {
        super(ActivityFilterData.class);

        // Создание формы для фильтрации активности.
        ActivityForm form = new ActivityForm();

        // Привязка полей формы к данным фильтра.
        binder.forField(form.getType()).bind(ActivityFilterData::getType, ActivityFilterData::setType);
        binder.forField(form.getInterval()).bind(ActivityFilterData::getInterval, ActivityFilterData::setInterval);
        binder.forField(form.getPeriod()).bind(
                p -> new DateRangePicker.LocalDateRange(p.start, p.end),
                (p, v) -> {
                    if (Objects.nonNull(v)) {
                        p.setStart(v.getStartDate());
                        p.setEnd(v.getEndDate());
                    }
                }
        );

        // Добавление слушателя изменений значений в форме.
        binder.addValueChangeListener(ignoredEvent -> {
            var s = new ActivityFilterData();
            binder.writeBeanIfValid(s);
            presenter.onUpdateFilter(s);
        });

        // Инициализация представления с формой, презентером и начальными данными фильтра.
        initView(form, presenter, new ActivityFilterData());
    }

    /**
     * Вложенный класс, представляющий данные фильтра для активности.
     */
    @Data
    public static class ActivityFilterData {
        private ActivityType type;
        private LocalDate start = LocalDate.now();
        private LocalDate end = LocalDate.now().plusWeeks(1);
        private ActivityIntervals interval;
    }
}