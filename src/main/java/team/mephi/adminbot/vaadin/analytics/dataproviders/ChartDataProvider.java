package team.mephi.adminbot.vaadin.analytics.dataproviders;

import com.vaadin.flow.data.provider.AbstractDataProvider;
import com.vaadin.flow.data.provider.Query;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.dataset.BarDataset;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.views.ActivityView;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class ChartDataProvider extends AbstractDataProvider<BarData, ActivityView.SimpleData> {
    @Override
    public boolean isInMemory() {
        return false;
    }

    @Override
    public int size(Query<BarData, ActivityView.SimpleData> query) {
        return 0;
    }

    @Override
    public Stream<BarData> fetch(Query<BarData, ActivityView.SimpleData> query) {
        var data = query.getFilter().orElse(new ActivityView.SimpleData());
        Random random = new Random();

        var labels = List.of("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
        if (ActivityIntervals.MONTH.name().equals(data.getInterval())) {
            labels = List.of("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
        } else if (ActivityIntervals.DAY.name().equals(data.getInterval())) {
            labels = List.of("12.01.2026", "13.01.2026", "14.01.2026", "15.01.2026", "16.01.2026", "17.01.2026", "18.01.2026", "19.01.2026", "20.01.2026", "21.01.2026", "22.01.2026", "23.01.2026");
        } else if (ActivityIntervals.HOUR.name().equals(data.getInterval())) {
            labels = List.of("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
        }

        var values = labels.stream().map(s1 -> random.nextInt(0, 100)).toList();

        BarData barData = new BarData().addLabels(labels.toArray(new String[0]));
        BarDataset dataset = new BarDataset().setLabel("Активность").setBackgroundColor("#2168df");
        values.forEach(dataset::addData);
        barData.addDataset(dataset);

        return Stream.of(barData);
    }
}
