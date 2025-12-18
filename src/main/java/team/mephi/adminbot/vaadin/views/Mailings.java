package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.mailings.service.MailingCountService;
import team.mephi.adminbot.vaadin.mailings.service.MailingPresenterFactory;
import team.mephi.adminbot.vaadin.mailings.service.MailingViewCallback;
import team.mephi.adminbot.vaadin.mailings.service.MailingsPresenter;
import team.mephi.adminbot.vaadin.mailings.tabs.MailingTabProvider;

import java.util.Comparator;
import java.util.List;


@Route(value = "/mailings", layout = DialogsLayout.class)
@RolesAllowed("ADMIN")
public class Mailings extends VerticalLayout implements MailingViewCallback {
    private static final String DELETE_TITLE = "Удалить рассылку?";
    private static final String DELETE_TEXT = "Вы действительно хотите удалить рассылку?";
    private static final String DELETE_ALL_TITLE = "Удалить рассылки?";
    private static final String DELETE_ALL_TEXT = "Вы действительно хотите удалить %d рассылок?";
    private static final String DELETE_ACTION = "Удалить";

    private final TabSheet tabSheet = new TabSheet();

    private final UserConfirmDialog dialogDelete;

    public Mailings(
            List<MailingTabProvider> tabProviders,
            MailingPresenterFactory presenterFactory,
            MailingCountService mailingCountService
    ) {
        this.dialogDelete = new UserConfirmDialog(
                DELETE_TITLE, DELETE_TEXT, DELETE_ACTION,
                DELETE_ALL_TITLE, DELETE_ALL_TEXT,
                null
        );

        setHeightFull();
        tabSheet.setSizeFull();
        add(createHeader(), tabSheet);

        tabProviders.sort(Comparator.comparingInt(MailingTabProvider::getPosition));

        for (var provider : tabProviders) {
            var tabId = provider.getTabId();
            var dataProvider = presenterFactory.createDataProvider(tabId);
            var presenter = new MailingsPresenter(dataProvider, this);
            var content = provider.createTabContent(presenter);

//            rolesInOrder.add(tabId);
//            dataProviders.put(tabId, dataProvider);
//            actions.put(tabId, presenter);

            var userCount = mailingCountService.getAllCounts().getOrDefault(provider.getTabId(), 0L);
            Span tabContent = new Span(new Span(provider.getTabLabel()), new UserCountBadge(userCount));
            tabSheet.add(new Tab(tabContent), content, provider.getPosition());
        }
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.addToStart(new H1("Рассылки"));

        var primaryButton = new Button("Новая рассылка", new Icon(VaadinIcon.PLUS), e -> {
//            getCurrentAction().onCreate(getCurrentRole());
        });
        primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        top.addToEnd(primaryButton);
        return top;
    }

    @Override
    public void showUserEditorForView(SimpleUser user) {

    }

    @Override
    public void showUserEditorForEdit(SimpleUser user) {

    }

    @Override
    public void showUserEditorForNew(String role) {

    }

    @Override
    public void confirmDelete(List<Long> ids, Runnable onConfirm) {
        dialogDelete.setCount(ids.size());
        dialogDelete.setOnConfirm(onConfirm);
        dialogDelete.open();
    }

    @Override
    public void confirmAccept(List<Long> ids, Runnable onConfirm) {

    }

    @Override
    public void confirmReject(List<Long> ids, Runnable onConfirm) {

    }

    @Override
    public void showNotification(String message) {
        Notification.show(message, 3000, Notification.Position.TOP_END);
    }
}
