package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.mailings.dataproviders.DraftDataProvider;
import team.mephi.adminbot.vaadin.mailings.views.DraftView;

@SpringComponent
public class DraftTabProvider implements MailingTabProvider {
    @Override
    public String getTabId() {
        return "draft";
    }

    @Override
    public String getTabLabel() {
        return "mailing_page_tab_draft_label";
    }

    @Override
    public Component createTabContent(CRUDDataProvider<?> mailingDataProvider, CRUDActions actions) {
        return new DraftView((DraftDataProvider) mailingDataProvider, actions);
    }

    @Override
    public Integer getPosition() {
        return 2;
    }
}
