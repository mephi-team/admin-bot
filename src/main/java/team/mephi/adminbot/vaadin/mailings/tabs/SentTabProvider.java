package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.mailings.actions.MailingActions;
import team.mephi.adminbot.vaadin.mailings.dataproviders.SentDataProvider;
import team.mephi.adminbot.vaadin.mailings.views.SentView;

@SpringComponent
public class SentTabProvider implements  MailingTabProvider {
    private final SentDataProvider mailingDataProvider;

    public SentTabProvider(SentDataProvider mailingDataProvider) {
        this.mailingDataProvider = mailingDataProvider;
    }
    @Override
    public String getTabId() {
        return "sent";
    }

    @Override
    public String getTabLabel() {
        return "Отправленные";
    }

    @Override
    public Component createTabContent(MailingActions actions) {
        return new SentView(mailingDataProvider, actions);
    }

    @Override
    public Integer getPosition() {
        return 0;
    }
}
