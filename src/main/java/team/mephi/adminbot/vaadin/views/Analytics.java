package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("/analytics")
@RolesAllowed("ADMIN")
public class Analytics extends VerticalLayout {
    public Analytics() {
        setSizeFull();

        add(new H1("Аналитика"));
    }
}
