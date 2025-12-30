package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.mailings.presenter.MailingsPresenter;
import team.mephi.adminbot.vaadin.mailings.views.DraftView;

@SpringComponent
public class DraftTabProvider implements MailingTabProvider {
    @Override
    public String getTabId() {
        return "draft";
    }

    @Override
    public String getTabLabel() {
        return "page_mailing_tab_draft_label";
    }

    @Override
    public Component createTabContent(CRUDActions actions) {
        return new DraftView((MailingsPresenter) actions);
    }

    @Override
    public Integer getPosition() {
        return 2;
    }
}
