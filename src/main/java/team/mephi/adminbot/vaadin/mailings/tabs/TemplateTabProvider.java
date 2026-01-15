package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.mailings.views.TemplateView;

import static team.mephi.adminbot.vaadin.mailings.tabs.MailingTabType.TEMPLATES;

@SpringComponent
@UIScope
public class TemplateTabProvider implements MailingTabProvider {
    @Override
    public MailingTabType getTabId() {
        return TEMPLATES;
    }

    @Override
    public String getTabLabel() {
        return TEMPLATES.getTabLabelKey();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Component createTabContent(CRUDActions<?> actions) {
        return new TemplateView((CRUDPresenter<SimpleTemplate>) actions);
    }

    @Override
    public Integer getPosition() {
        return TEMPLATES.ordinal();
    }
}
