package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.mailings.presenter.MailingsPresenter;
import team.mephi.adminbot.vaadin.mailings.views.DraftView;

import static team.mephi.adminbot.vaadin.mailings.tabs.MailingTabType.DRAFT;

@SpringComponent
public class DraftTabProvider implements MailingTabProvider {
    @Override
    public MailingTabType getTabId() {
        return DRAFT;
    }

    @Override
    public String getTabLabel() {
        return DRAFT.getTabLabelKey();
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new DraftView((MailingsPresenter) actions);
    }

    @Override
    public Integer getPosition() {
        return DRAFT.ordinal();
    }
}
