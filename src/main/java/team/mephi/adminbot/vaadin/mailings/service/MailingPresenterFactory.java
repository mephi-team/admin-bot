package team.mephi.adminbot.vaadin.mailings.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.mailings.dataproviders.MailingDataProvider;

import java.util.Map;

@SpringComponent
public class MailingPresenterFactory {
    private final Map<String, MailingDataProvider<SimpleMailing>> providers;

    public MailingPresenterFactory(Map<String, MailingDataProvider<SimpleMailing>> providers) {
        this.providers = providers;
    }

    public MailingDataProvider<SimpleMailing> createDataProvider(String role) {
        return providers.get(role);
    }
}
