package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.mailings.dataproviders.DraftDataProvider;
import team.mephi.adminbot.vaadin.mailings.views.DraftView;

@SpringComponent
public class DraftTabProvider implements MailingTabProvider {
    private final DraftDataProvider mailingDataProvider;

    public DraftTabProvider(DraftDataProvider mailingDataProvider) {
        this.mailingDataProvider = mailingDataProvider;
    }
    @Override
    public String getTabId() {
        return "draft";
    }

    @Override
    public String getTabLabel() {
        return "Черновики";
    }

    @Override
    public Component createTabContent(CRUDActions actions) {
        return new DraftView(mailingDataProvider, actions);
    }

    @Override
    public Integer getPosition() {
        return 2;
    }
}
