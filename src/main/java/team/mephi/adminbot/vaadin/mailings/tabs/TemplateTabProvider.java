package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.mailings.dataproviders.TemplateDataProvider;
import team.mephi.adminbot.vaadin.mailings.views.TemplateView;

@SpringComponent
public class TemplateTabProvider implements MailingTabProvider {
    private final TemplateDataProvider templateDataProvider;

    public TemplateTabProvider(TemplateDataProvider templateDataProvider) {
        this.templateDataProvider = templateDataProvider;
    }

    @Override
    public String getTabId() {
        return "templates";
    }

    @Override
    public String getTabLabel() {
        return "Шаблоны";
    }

    @Override
    public Component createTabContent(CRUDActions actions) {
        return new TemplateView(templateDataProvider, actions);
    }

    @Override
    public Integer getPosition() {
        return 1;
    }
}
