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
import team.mephi.adminbot.vaadin.mailings.dataproviders.MailingDataProvider;
import team.mephi.adminbot.vaadin.mailings.service.MailingCountService;
import team.mephi.adminbot.vaadin.mailings.service.MailingPresenterFactory;
import team.mephi.adminbot.vaadin.mailings.service.MailingViewCallback;
import team.mephi.adminbot.vaadin.mailings.service.MailingsPresenter;
import team.mephi.adminbot.vaadin.mailings.tabs.MailingTabProvider;

import java.util.*;


@Route(value = "/mailings", layout = DialogsLayout.class)
@RolesAllowed("ADMIN")
public class Mailings extends VerticalLayout {
    private final TabSheet tabSheet = new TabSheet();
    private final Button primaryButton = new Button(getTranslation("create_mailing_button"), new Icon(VaadinIcon.PLUS));

    private final MailingEditorDialog mailingEditorDialog;
    private final TemplateEditorDialog templateEditorDialog;
    private final SimpleConfirmDialog dialogDelete;
    private final SimpleConfirmDialog dialogCancel;
    private final SimpleConfirmDialog dialogRetry;

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
        this.dialogDelete = new SimpleConfirmDialog(
                "delete_mailing_title", "delete_mailing_text", "delete_mailing_action",
                "delete_mailing_all_title", "delete_mailing_all_text"
        );
        this.dialogCancel = new SimpleConfirmDialog(
                "cancel_mailing_title", "cancel_mailing_text", "cancel_mailing_action"
        );
        this.dialogRetry = new SimpleConfirmDialog(
                "retry_mailing_title", "retry_mailing_text", "retry_mailing_action"
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
                        return templateEditorDialog.getEditedItem();
                    }
                    @Override
                    public void showDialogForEdit(SimpleTemplate mailing) {
                        templateEditorDialog.setHeaderTitle("template_edit_title");
                        templateEditorDialog.showDialogForEdit(mailing);
                    }
                    @Override
                    public void showDialogForNew(String role) {
                        templateEditorDialog.setHeaderTitle("template_new_title");
                        templateEditorDialog.showDialogForNew();
                    }
                    @Override
                    public void confirmDelete(List<Long> ids, Runnable onConfirm) {
                        dialogDelete.showForConfirm(ids.size(), onConfirm);
                    }
                    @Override
                    public void showNotificationForNew() {
                        Notification.show(getTranslation("mailing_created"), 3000, Notification.Position.TOP_END);
                    }
                    @Override
                    public void showNotificationForEdit(Long id) {
                        Notification.show(getTranslation("mailing_saved"), 3000, Notification.Position.TOP_END);
                    }
                    @Override
                    public void showNotificationForDelete(List<Long> ids) {
                        String message = getTranslation("delete_message");
                        if (ids.size() > 1) {
                            message = getTranslation("delete_all_message", ids.size());
                        }
                        Notification.show(message, 3000, Notification.Position.TOP_END);
                    }
                });
            } else {
                MailingDataProvider<SimpleMailing> dataProvider = (MailingDataProvider<SimpleMailing>) presenterFactory.createDataProvider(tabId);
                presenter = new MailingsPresenter(dataProvider, new MailingViewCallback() {
                    @Override
                    public void confirmCancel(Long id, Runnable onConfirm) {
                        dialogCancel.showForConfirm(1, onConfirm);
                    }

                    @Override
                    public void confirmRetry(Long id, Runnable onConfirm) {
                        dialogRetry.showForConfirm(1, onConfirm);
                    }

                    @Override
                    public void showNotificationForCancel(Long id) {
                        Notification.show(getTranslation("cancel_message"), 3000, Notification.Position.TOP_END);
                    }

                    @Override
                    public void showNotificationForRetry(Long id) {
                        Notification.show(getTranslation("retry_message"), 3000, Notification.Position.TOP_END);
                    }

                    @Override
                    public void setOnSaveCallback(SerializableRunnable callback) {
                        mailingEditorDialog.setOnSaveCallback(callback);
                    }
                    @Override
                    public SimpleMailing getEditedItem() {
                        return mailingEditorDialog.getEditedItem();
                    }

                    @Override
                    public void showDialogForView(SimpleMailing user) {

                    }

                    @Override
                    public void showDialogForEdit(SimpleMailing mailing) {
                        mailingEditorDialog.setHeaderTitle("mailing_edit_title");
                        mailingEditorDialog.showDialogForEdit(mailing);
                    }
                    @Override
                    public void showDialogForNew(String role) {
                        mailingEditorDialog.setHeaderTitle("mailing_new_title");
                        mailingEditorDialog.showDialogForNew();
                    }

                    @Override
                    public void confirmDelete(List<Long> ids, Runnable onConfirm) {
                        dialogDelete.showForConfirm(ids.size(), onConfirm);
                    }
                    @Override
                    public void showNotificationForNew() {
                        Notification.show(getTranslation("mailing_created"), 3000, Notification.Position.TOP_END);
                    }
                    @Override
                    public void showNotificationForEdit(Long id) {
                        Notification.show(getTranslation("mailing_saved"), 3000, Notification.Position.TOP_END);
                    }
                    @Override
                    public void showNotificationForDelete(List<Long> ids) {
                        String message = getTranslation("delete_message");
                        if (ids.size() > 1) {
                            message = getTranslation("delete_all_message", ids.size());
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
        tabSheet.addSelectedChangeListener(a -> {
           if (tabSheet.getSelectedIndex() == 1) {
               primaryButton.setText(getTranslation("create_template_button"));
           } else {
               primaryButton.setText(getTranslation("create_mailing_button"));
           }
        });
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.addToStart(new H1(getTranslation("mailing_page_title")));

        primaryButton.addClickListener(e -> {
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
