package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.mailings.actions.MailingActions;
import team.mephi.adminbot.vaadin.mailings.views.DraftView;

@SpringComponent
public class DraftTabProvider implements MailingTabProvider {
    @Override
    public String getTabId() {
        return "draft";
    }

    @Override
    public String getTabLabel() {
        return "Черновики";
    }

    @Override
    public Component createTabContent(MailingActions actions) {
        return new DraftView();
    }

    @Override
    public Integer getPosition() {
        return 2;
    }
}
