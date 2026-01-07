package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.mailings.presenter.TemplatePresenter;
import team.mephi.adminbot.vaadin.mailings.views.TemplateView;

@SpringComponent
@UIScope
public class TemplateTabProvider implements MailingTabProvider {
    @Override
    public String getTabId() {
        return "templates";
    }

    @Override
    public String getTabLabel() {
        return "page_mailing_tab_templates_label";
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new TemplateView((TemplatePresenter) actions);
    }

    @Override
    public Integer getPosition() {
        return 1;
    }
}
