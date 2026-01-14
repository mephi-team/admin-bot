package team.mephi.adminbot.service;

import org.springframework.stereotype.Service;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.dataset.BarDataset;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.views.ActivityView;
import team.mephi.adminbot.vaadin.analytics.views.OrdersView;
import team.mephi.adminbot.vaadin.analytics.views.PreordersView;
import team.mephi.adminbot.vaadin.analytics.views.UtmView;

import java.util.List;
import java.util.Random;

@Service
public class ChartDataServiceImpl implements ChartDataService {
    private static final List<String> MONTH_LABELS = List.of("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
    private static final List<String> DAY_LABELS = List.of("12.01.2026", "13.01.2026", "14.01.2026", "15.01.2026", "16.01.2026", "17.01.2026", "18.01.2026", "19.01.2026", "20.01.2026", "21.01.2026", "22.01.2026", "23.01.2026");
    private static final List<String> HOUR_LABELS = List.of("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");

    private List<String> labelsForInterval(String interval) {
        if (ActivityIntervals.HOUR.name().equals(interval)) {
            return HOUR_LABELS;
        } else if (ActivityIntervals.DAY.name().equals(interval)) {
            return DAY_LABELS;
        }
        return MONTH_LABELS;
    }

    private BarDataset createDataset(String label, String backgroundColor, List<String> labels, Random random) {
        BarDataset dataset = new BarDataset().setLabel(label);
        if (backgroundColor != null) {
            dataset.setBackgroundColor(backgroundColor);
        }
        labels.stream().map(s -> random.nextInt(0, 100)).forEach(dataset::addData);
        return dataset;
    }

    @Override
    public BarData forActivity(ActivityView.ActivityFilterData data) {
        Random random = new Random();
        var labels = labelsForInterval(data.getInterval());
        BarData barData = new BarData().addLabels(labels.toArray(new String[0]));
        barData.addDataset(createDataset("Активность", "#2168df", labels, random));
        return barData;
    }

    @Override
    public BarData forPreorders(PreordersView.PreorderFilterData data) {
        Random random = new Random();
        var labels = labelsForInterval(data.getInterval());
        BarData barData = new BarData().addLabels(labels.toArray(new String[0]));
        barData.addDataset(createDataset("Подано всего заявок", "#2168df", labels, random));
        barData.addDataset(createDataset("Актуальные заявки", "#d3e1f9", labels, random));
        barData.addDataset(createDataset("Отозванные заявки", null, labels, random));
        return barData;
    }

    @Override
    public BarData forOrders(OrdersView.OrderFilterData data) {
        Random random = new Random();
        var labels = labelsForInterval(data.getInterval());
        BarData barData = new BarData().addLabels(labels.toArray(new String[0]));
        barData.addDataset(createDataset("Подано всего заявок", "#2168df", labels, random));
        barData.addDataset(createDataset("Актуальные заявки", "#d3e1f9", labels, random));
        barData.addDataset(createDataset("Отозванные заявки", null, labels, random));
        return barData;
    }

    @Override
    public BarData forUtm(UtmView.UtmFilterData data) {
        Random random = new Random();
        var labels = labelsForInterval(data.getInterval());
        BarData barData = new BarData().addLabels(labels.toArray(new String[0]));
        barData.addDataset(createDataset("UTM", "#2168df", labels, random));
        return barData;
    }
}
