package team.mephi.adminbot.service;

import org.springframework.stereotype.Service;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.dataset.BarDataset;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.views.ActivityView;
import team.mephi.adminbot.vaadin.analytics.views.OrdersView;
import team.mephi.adminbot.vaadin.analytics.views.PreordersView;
import team.mephi.adminbot.vaadin.analytics.views.UtmView;

@Service
public class ChartDataServiceImpl implements ChartDataService {
    private static final String[] MONTH_LABELS = new String[]{"Январь","Февраль","Март","Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь"};
    private static final String[] DAY_LABELS = new String[]{"12.01.2026","13.01.2026","14.01.2026","15.01.2026","16.01.2026","17.01.2026","18.01.2026","19.01.2026","20.01.2026","21.01.2026","22.01.2026","23.01.2026"};
    private static final String[] HOUR_LABELS = new String[]{"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
    private static final String DEFAULT_BLUE = "#2168df";

    private final java.util.Random random = new java.util.Random();

    private String[] labelsForInterval(String interval) {
        try {
            ActivityIntervals ai = ActivityIntervals.valueOf(interval);
            switch (ai) {
                case HOUR: return HOUR_LABELS;
                case DAY: return DAY_LABELS;
                default: return MONTH_LABELS;
            }
        } catch (Exception e) {
            return MONTH_LABELS;
        }
    }

    private BarDataset createDataset(String label, String backgroundColor, String[] labels) {
        BarDataset dataset = new BarDataset().setLabel(label);
        if (backgroundColor != null) {
            dataset.setBackgroundColor(backgroundColor);
        }
        for (int i = 0; i < labels.length; i++) {
            dataset.addData(random.nextInt(0, 100));
        }
        return dataset;
    }

    private record DatasetSpec(String label, String color) {}

    private BarData createBarData(String[] labels, DatasetSpec... specs) {
        BarData barData = new BarData().addLabels(labels);
        for (DatasetSpec spec : specs) {
            barData.addDataset(createDataset(spec.label(), spec.color(), labels));
        }
        return barData;
    }

    @Override
    public BarData forActivity(ActivityView.ActivityFilterData data) {
        String[] labels = labelsForInterval(data.getInterval());
        return createBarData(labels, new DatasetSpec("Активность", DEFAULT_BLUE));
    }

    @Override
    public BarData forPreorders(PreordersView.PreorderFilterData data) {
        String[] labels = labelsForInterval(data.getInterval());
        return createBarData(labels,
                new DatasetSpec("Подано всего заявок", DEFAULT_BLUE),
                new DatasetSpec("Актуальные заявки", "#d3e1f9"),
                new DatasetSpec("Отозванные заявки", null));
    }

    @Override
    public BarData forOrders(OrdersView.OrderFilterData data) {
        String[] labels = labelsForInterval(data.getInterval());
        return createBarData(labels,
                new DatasetSpec("Подано всего заявок", DEFAULT_BLUE),
                new DatasetSpec("Актуальные заявки", "#d3e1f9"),
                new DatasetSpec("Отозванные заявки", null));
    }

    @Override
    public BarData forUtm(UtmView.UtmFilterData data) {
        String[] labels = labelsForInterval(data.getInterval());
        return createBarData(labels, new DatasetSpec("UTM", DEFAULT_BLUE));
    }
}
