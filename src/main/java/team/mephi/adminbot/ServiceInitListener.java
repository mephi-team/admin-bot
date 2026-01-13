package team.mephi.adminbot;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

import java.util.Locale;

public class ServiceInitListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(sessionEvent -> {
            Locale preferredLocale = sessionEvent.getRequest().getLocale();
            sessionEvent.getSession().setLocale(preferredLocale);
        });
    }
}
