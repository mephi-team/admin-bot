package team.mephi.adminbot.vaadin.analytics.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import software.xdev.vaadin.chartjs.ChartContainer;

public class ActivityView extends VerticalLayout {
    public ActivityView() {
        ChartContainer chart = new ChartContainer();
        this.add(chart);

        chart.showChart(
                "{\"data\":{\"labels\":[\"A\",\"B\"],\"datasets\":[{\"data\":[1,2],\"label\":\"X\"}]},\"type\":\"bar\"}");
    }
}
