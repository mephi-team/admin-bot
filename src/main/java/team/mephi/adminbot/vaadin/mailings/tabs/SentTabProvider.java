package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.mailings.presenter.MailingsPresenter;
import team.mephi.adminbot.vaadin.mailings.views.SentView;

@SpringComponent
public class SentTabProvider implements  MailingTabProvider {
    @Override
    public String getTabId() {
        return "sent";
    }

    @Override
    public String getTabLabel() {
        return "page_mailing_tab_sent_label";
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new SentView((MailingsPresenter) actions);
    }

    @Override
    public Integer getPosition() {
        return 0;
    }
}
