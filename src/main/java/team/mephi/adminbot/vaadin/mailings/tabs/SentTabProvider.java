package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.mailings.presenter.MailingsPresenter;
import team.mephi.adminbot.vaadin.mailings.views.SentView;

import static team.mephi.adminbot.vaadin.mailings.tabs.MailingTabType.SENT;

@SpringComponent
public class SentTabProvider implements MailingTabProvider {
    @Override
    public MailingTabType getTabId() {
        return SENT;
    }

    @Override
    public String getTabLabel() {
        return SENT.getTabLabelKey();
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new SentView((MailingsPresenter) actions);
    }

    @Override
    public Integer getPosition() {
        return SENT.ordinal();
    }
}
