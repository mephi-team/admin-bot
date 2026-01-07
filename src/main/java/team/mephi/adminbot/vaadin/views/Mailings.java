package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.vaadin.*;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.mailings.service.MailingCountService;
import team.mephi.adminbot.vaadin.mailings.presenter.MailingPresenterFactory;
import team.mephi.adminbot.vaadin.mailings.tabs.MailingTabProvider;

import java.util.*;


@Route(value = "/mailings", layout = DialogsLayout.class)
@RolesAllowed("ADMIN")
public class Mailings extends VerticalLayout {
    private final TabSheet tabSheet = new TabSheet();
    private final Button primaryButton = new Button(getTranslation("page_mailing_create_mailing_button"), VaadinIcon.PLUS.create());

    private final List<String> rolesInOrder = new ArrayList<>();
    private final Map<String, CRUDActions<?>> actions = new HashMap<>();

    public Mailings(
            List<MailingTabProvider> tabProviders,
            MailingPresenterFactory presenterFactory,
            MailingCountService mailingCountService
    ) {
        setHeightFull();
        tabSheet.setSizeFull();
        add(createHeader(), tabSheet);

        tabProviders.sort(Comparator.comparingInt(MailingTabProvider::getPosition));

        for (var provider : tabProviders) {
            var tabId = provider.getTabId();
            var presenter = presenterFactory.createPresenter(tabId);
            var content = provider.createTabContent(presenter);

            rolesInOrder.add(tabId);
            actions.put(tabId, presenter);

            var userCount = mailingCountService.getAllCounts().getOrDefault(provider.getTabId(), 0L);
            Span tabContent = new Span(new Span(getTranslation(provider.getTabLabel())), new UserCountBadge(userCount));
            tabSheet.add(new Tab(tabContent), content, provider.getPosition());
        }
        tabSheet.addSelectedChangeListener(a -> {
           if (tabSheet.getSelectedIndex() == 1) {
               primaryButton.setText(getTranslation("page_mailing_create_template_button"));
           } else {
               primaryButton.setText(getTranslation("page_mailing_create_mailing_button"));
           }
        });
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.addToStart(new H1(getTranslation("page_mailing_title")));

        primaryButton.addClickListener(e -> {
            getCurrentAction().onCreate(null, getCurrentRole()+"_created");
        });
        primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        top.addToEnd(primaryButton);
        return top;
    }

    private String getCurrentRole() {
        var selectedTab = tabSheet.getSelectedIndex();
        if (selectedTab > -1) {
            return rolesInOrder.get(selectedTab);
        }
        return "visitor";
    }

    private CRUDActions<?> getCurrentAction() {
        return actions.get(getCurrentRole());
    }
}
