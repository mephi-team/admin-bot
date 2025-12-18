package team.mephi.adminbot.vaadin.mailings.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.mailings.dataproviders.SentDataProvider;

import java.util.Map;

@SpringComponent
public class MailingPresenterFactory {
    private final Map<String, SentDataProvider> providers;

    public MailingPresenterFactory(Map<String, SentDataProvider> providers) {
        this.providers = providers;
    }

    public SentDataProvider createDataProvider(String role) {
        return providers.get(role);
    }
}
