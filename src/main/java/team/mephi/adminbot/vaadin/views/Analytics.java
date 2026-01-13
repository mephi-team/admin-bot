package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import software.xdev.vaadin.chartjs.ChartContainer;

@Route("/analytics")
@RolesAllowed("ADMIN")
public class Analytics extends VerticalLayout {
    public Analytics() {
        setSizeFull();
        getElement().getStyle().set("padding-inline", "120px");
        add(new H1(getTranslation("page_analytics_title")));

        ChartContainer chart = new ChartContainer();
        this.add(chart);

        chart.showChart(
                "{\"data\":{\"labels\":[\"A\",\"B\"],\"datasets\":[{\"data\":[1,2],\"label\":\"X\"}]},\"type\":\"bar\"}");
    }
}
