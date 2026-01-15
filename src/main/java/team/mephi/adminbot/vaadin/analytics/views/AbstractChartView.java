package team.mephi.adminbot.vaadin.analytics.views;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataChangeEvent;
import software.xdev.chartjs.model.charts.BarChart;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.options.BarOptions;
import software.xdev.chartjs.model.options.LegendOptions;
import software.xdev.vaadin.chartjs.ChartContainer;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;

/**
 * Базовая вью с общим поведением для отображения ChartJs графика.
 * Подклассы:
 * - настраивают биндинг (binder)
 * - создают/передают форму в initView(...)
 * - вызывают initView(form, presenter, initialFilter)
 */
public abstract class AbstractChartView<F> extends VerticalLayout {
    protected final BeanValidationBinder<F> binder;
    protected final ChartContainer chart = new ChartContainer();

    /**
     * Конструктор.
     *
     * @param filterClass класс фильтра
     */
    protected AbstractChartView(Class<F> filterClass) {
        this.binder = new BeanValidationBinder<>(filterClass);
        setPadding(false);
    }

    /**
     * Общая инициализация layout + подписка на обновления данных.
     * Subclass должен подготовить binder (bind поля) и добавить value-change listener,
     * который будет вызывать presenter.onUpdateFilter(...)
     */
    protected void initView(com.vaadin.flow.component.Component form, ChartPresenter<F> presenter, F initialFilter) {
        VerticalLayout column = new VerticalLayout();
        column.setPadding(false);
        column.setWidth("640px");
        column.add(form);

        HorizontalLayout content = new HorizontalLayout();
        content.setWidthFull();
        content.add(chart, column);
        add(content);

        var buttonGroup = new HorizontalLayout(
                new SecondaryButton(getTranslation("page_analytics_form_activity_download_png_action"), VaadinIcon.DOWNLOAD_ALT.create()),
                new SecondaryButton(getTranslation("page_analytics_form_activity_download_excel_action"), VaadinIcon.DOWNLOAD_ALT.create())
        );
        add(buttonGroup);

        presenter.getDataProvider().addDataProviderListener(event -> {
            if (event instanceof DataChangeEvent.DataRefreshEvent) {
                var data = ((DataChangeEvent.DataRefreshEvent<BarData>) event).getItem();
                updateChart(data);
            }
        });

        presenter.onUpdateFilter(initialFilter);
    }

    /**
     * Обновление данных графика.
     */
    protected void updateChart(BarData data) {
        BarOptions options = new BarOptions();
        options.getPlugins().setLegend(new LegendOptions().setAlign("start").setPosition("bottom"));
        chart.showChart(new BarChart(data, options).toJson());
    }

    /**
     * @return биндер формы фильтра
     */
    protected BeanValidationBinder<F> getBinder() {
        return binder;
    }
}