package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/analytics")
public class Analytics extends VerticalLayout {
    public Analytics() {
        add(new H1("Analytics"));
    }
}
