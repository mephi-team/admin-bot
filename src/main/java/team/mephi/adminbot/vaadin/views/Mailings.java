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
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.vaadin.*;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.mailings.components.MailingEditorDialog;
import team.mephi.adminbot.vaadin.mailings.components.MailingEditorDialogFactory;
import team.mephi.adminbot.vaadin.mailings.components.TemplateEditorDialog;
import team.mephi.adminbot.vaadin.mailings.components.TemplateEditorDialogFactory;
import team.mephi.adminbot.vaadin.mailings.service.MailingCountService;
import team.mephi.adminbot.vaadin.mailings.service.MailingPresenterFactory;
import team.mephi.adminbot.vaadin.mailings.tabs.MailingTabProvider;

import java.util.*;


@Route(value = "/mailings", layout = DialogsLayout.class)
@RolesAllowed("ADMIN")
public class Mailings extends VerticalLayout {
    private static final String DELETE_TITLE = "Удалить рассылку?";
    private static final String DELETE_TEXT = "Вы действительно хотите удалить рассылку?";
    private static final String DELETE_ALL_TITLE = "Удалить рассылки?";
    private static final String DELETE_ALL_TEXT = "Вы действительно хотите удалить %d рассылок?";
    private static final String DELETE_ACTION = "Удалить";

    private static final String MAILING_CREATED = "Рассылка сохранена";
    private static final String MAILING_SAVED = "Рассылка сохранена";
    private static final String DELETE_MESSAGE = "Рассылка удалена";
    private static final String DELETE_ALL_MESSAGE = "Удалено %d рассылок";

    private final TabSheet tabSheet = new TabSheet();

    private final MailingEditorDialog mailingEditorDialog;
    private final TemplateEditorDialog templateEditorDialog;
    private final UserConfirmDialog dialogDelete;

    private final List<String> rolesInOrder = new ArrayList<>();
    private final Map<String, CRUDActions> actions = new HashMap<>();

    private static final CRUDActions NO_OP_ACTIONS = new CRUDActions() {
        @Override public void onCreate(String role) {}
        @Override public void onView(Long id) {}
        @Override public void onEdit(Long id) {}
        @Override public void onDelete(List<Long> ids) {}
    };

    public Mailings(
            List<MailingTabProvider> tabProviders,
            MailingPresenterFactory presenterFactory,
            MailingEditorDialogFactory mailingDialogFactory,
            TemplateEditorDialogFactory templateDialogFactory,
            MailingCountService mailingCountService
    ) {
        this.dialogDelete = new UserConfirmDialog(
                DELETE_TITLE, DELETE_TEXT, DELETE_ACTION,
                DELETE_ALL_TITLE, DELETE_ALL_TEXT,
                null
        );
        this.mailingEditorDialog = mailingDialogFactory.create();
        this.templateEditorDialog = templateDialogFactory.create();

        setHeightFull();
        tabSheet.setSizeFull();
        add(createHeader(), tabSheet, mailingEditorDialog);

        tabProviders.sort(Comparator.comparingInt(MailingTabProvider::getPosition));

        for (var provider : tabProviders) {
            var tabId = provider.getTabId();
            CRUDPresenter<?> presenter;
            if (tabId.equals("templates")) {
                CRUDDataProvider<SimpleTemplate> dataProvider = (CRUDDataProvider<SimpleTemplate>) presenterFactory.createDataProvider(tabId);
                presenter = new CRUDPresenter<>(dataProvider, new CRUDViewCallbackBase<>() {
                    @Override
                    public void setOnSaveCallback(SerializableRunnable callback) {
                        templateEditorDialog.setOnSaveCallback(callback);
                    }
                    @Override
                    public SimpleTemplate getEditedItem() {
                        return templateEditorDialog.getEditedMailing();
                    }
                    @Override
                    public void showDialogForEdit(SimpleTemplate mailing) {
                        templateEditorDialog.openForEdit(mailing);
                    }
                    @Override
                    public void showDialogForNew(String role) {
                        mailingEditorDialog.openForNew();
                    }
                    @Override
                    public void confirmDelete(List<Long> ids, Runnable onConfirm) {
                        dialogDelete.setCount(ids.size());
                        dialogDelete.setOnConfirm(onConfirm);
                        dialogDelete.open();
                    }
                    @Override
                    public void showNotificationForNew() {
                        Notification.show(MAILING_CREATED, 3000, Notification.Position.TOP_END);
                    }
                    @Override
                    public void showNotificationForEdit(Long id) {
                        Notification.show(MAILING_SAVED, 3000, Notification.Position.TOP_END);
                    }
                    @Override
                    public void showNotificationForDelete(List<Long> ids) {
                        String message = DELETE_MESSAGE;
                        if (ids.size() > 1) {
                            message = String.format(DELETE_ALL_MESSAGE, ids.size());
                        }
                        Notification.show(message, 3000, Notification.Position.TOP_END);
                    }
                });
            } else {
                CRUDDataProvider<SimpleMailing> dataProvider = (CRUDDataProvider<SimpleMailing>) presenterFactory.createDataProvider(tabId);
                presenter = new CRUDPresenter<>(dataProvider, new CRUDViewCallbackBase<>() {
                    @Override
                    public void setOnSaveCallback(SerializableRunnable callback) {
                        mailingEditorDialog.setOnSaveCallback(callback);
                    }
                    @Override
                    public SimpleMailing getEditedItem() {
                        return mailingEditorDialog.getEditedMailing();
                    }
                    @Override
                    public void showDialogForEdit(SimpleMailing mailing) {
                        mailingEditorDialog.openForEdit(mailing);
                    }
                    @Override
                    public void showDialogForNew(String role) {
                        mailingEditorDialog.openForNew();
                    }
                    @Override
                    public void confirmDelete(List<Long> ids, Runnable onConfirm) {
                        dialogDelete.setCount(ids.size());
                        dialogDelete.setOnConfirm(onConfirm);
                        dialogDelete.open();
                    }
                    @Override
                    public void showNotificationForNew() {
                        Notification.show(MAILING_CREATED, 3000, Notification.Position.TOP_END);
                    }
                    @Override
                    public void showNotificationForEdit(Long id) {
                        Notification.show(MAILING_SAVED, 3000, Notification.Position.TOP_END);
                    }
                    @Override
                    public void showNotificationForDelete(List<Long> ids) {
                        String message = DELETE_MESSAGE;
                        if (ids.size() > 1) {
                            message = String.format(DELETE_ALL_MESSAGE, ids.size());
                        }
                        Notification.show(message, 3000, Notification.Position.TOP_END);
                    }
                });
            }
            var content = provider.createTabContent(presenter);

            rolesInOrder.add(tabId);
            actions.put(tabId, presenter);

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
            getCurrentAction().onCreate(getCurrentRole());
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

    private CRUDActions getCurrentAction() {
        return actions.getOrDefault(getCurrentRole(), NO_OP_ACTIONS);
    }
}
