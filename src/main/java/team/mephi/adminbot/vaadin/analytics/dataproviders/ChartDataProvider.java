package team.mephi.adminbot.vaadin.analytics.dataproviders;

import com.vaadin.flow.data.provider.AbstractDataProvider;
import com.vaadin.flow.data.provider.Query;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.dataset.BarDataset;
import team.mephi.adminbot.vaadin.analytics.components.ActivityIntervals;
import team.mephi.adminbot.vaadin.analytics.views.ActivityView;
import team.mephi.adminbot.vaadin.analytics.views.OrdersView;
import team.mephi.adminbot.vaadin.analytics.views.PreordersView;
import team.mephi.adminbot.vaadin.analytics.views.UtmView;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class ChartDataProvider<T> extends AbstractDataProvider<BarData, T> {
    @Override
    public boolean isInMemory() {
        return false;
    }

    @Override
    public int size(Query<BarData, T> query) {
        return 0;
    }

    @Override
    public Stream<BarData> fetch(Query<BarData, T> query) {
        Random random = new Random();

        var filter = query.getFilter().orElse(null);
        if (filter instanceof ActivityView.ActivityFilterData) {
            var data = (ActivityView.ActivityFilterData) filter;
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
        } else if (filter instanceof PreordersView.PreorderFilterData) {
            var data = (PreordersView.PreorderFilterData) filter;

            var labels = List.of("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
            if (ActivityIntervals.MONTH.name().equals(data.getInterval())) {
                labels = List.of("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
            } else if (ActivityIntervals.DAY.name().equals(data.getInterval())) {
                labels = List.of("12.01.2026", "13.01.2026", "14.01.2026", "15.01.2026", "16.01.2026", "17.01.2026", "18.01.2026", "19.01.2026", "20.01.2026", "21.01.2026", "22.01.2026", "23.01.2026");
            } else if (ActivityIntervals.HOUR.name().equals(data.getInterval())) {
                labels = List.of("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
            }

            BarData barData = new BarData().addLabels(labels.toArray(new String[0]));
            BarDataset dataset1 = new BarDataset().setLabel("Test1").setBackgroundColor("#2168df");
            labels.stream().map(s1 -> random.nextInt(0, 100)).forEach(dataset1::addData);
            barData.addDataset(dataset1);
            BarDataset dataset2 = new BarDataset().setLabel("Test2").setBackgroundColor("#d3e1f9");
            labels.stream().map(s1 -> random.nextInt(0, 100)).forEach(dataset2::addData);
            barData.addDataset(dataset2);
            BarDataset dataset3 = new BarDataset().setLabel("Test3");
            labels.stream().map(s1 -> random.nextInt(0, 100)).forEach(dataset3::addData);
            barData.addDataset(dataset3);

            return Stream.of(barData);
        } else if (filter instanceof OrdersView.OrderFilterData) {
            var data = (OrdersView.OrderFilterData) filter;

            var labels = List.of("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
            if (ActivityIntervals.MONTH.name().equals(data.getInterval())) {
                labels = List.of("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
            } else if (ActivityIntervals.DAY.name().equals(data.getInterval())) {
                labels = List.of("12.01.2026", "13.01.2026", "14.01.2026", "15.01.2026", "16.01.2026", "17.01.2026", "18.01.2026", "19.01.2026", "20.01.2026", "21.01.2026", "22.01.2026", "23.01.2026");
            } else if (ActivityIntervals.HOUR.name().equals(data.getInterval())) {
                labels = List.of("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
            }

            BarData barData = new BarData().addLabels(labels.toArray(new String[0]));
            BarDataset dataset1 = new BarDataset().setLabel("Test1").setBackgroundColor("#2168df");
            labels.stream().map(s1 -> random.nextInt(0, 100)).forEach(dataset1::addData);
            barData.addDataset(dataset1);
            BarDataset dataset2 = new BarDataset().setLabel("Test2").setBackgroundColor("#d3e1f9");
            labels.stream().map(s1 -> random.nextInt(0, 100)).forEach(dataset2::addData);
            barData.addDataset(dataset2);
            BarDataset dataset3 = new BarDataset().setLabel("Test3");
            labels.stream().map(s1 -> random.nextInt(0, 100)).forEach(dataset3::addData);
            barData.addDataset(dataset3);

            return Stream.of(barData);
        } else if (filter instanceof UtmView.UtmFilterData) {
            var data = (UtmView.UtmFilterData) filter;
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
            BarDataset dataset = new BarDataset().setLabel("UTM").setBackgroundColor("#2168df");
            values.forEach(dataset::addData);
            barData.addDataset(dataset);

            return Stream.of(barData);
        }
        return Stream.empty();
    }
}
