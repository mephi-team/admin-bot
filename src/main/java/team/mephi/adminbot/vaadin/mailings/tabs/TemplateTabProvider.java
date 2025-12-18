package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.mailings.actions.MailingActions;
import team.mephi.adminbot.vaadin.mailings.views.TemplateView;

@SpringComponent
public class TemplateTabProvider implements MailingTabProvider {

    @Override
    public String getTabId() {
        return "templates";
    }

    @Override
    public String getTabLabel() {
        return "Шаблоны";
    }

    @Override
    public Component createTabContent(MailingActions actions) {
        return new TemplateView();
    }

    @Override
    public Integer getPosition() {
        return 1;
    }
}
