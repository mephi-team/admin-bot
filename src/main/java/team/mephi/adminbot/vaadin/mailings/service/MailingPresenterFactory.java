package team.mephi.adminbot.vaadin.mailings.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.mailings.dataproviders.MailingDataProvider;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;

import java.util.Map;

@SpringComponent
public class MailingPresenterFactory {
    private final Map<String, MailingDataProvider> providers;

    public MailingPresenterFactory(Map<String, MailingDataProvider> providers) {
        this.providers = providers;
    }

    public MailingDataProvider createDataProvider(String role) {
        return providers.get(role);
    }
}
