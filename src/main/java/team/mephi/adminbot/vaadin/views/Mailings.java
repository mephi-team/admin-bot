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
import team.mephi.adminbot.vaadin.mailings.presenter.MailingPresenterFactory;
import team.mephi.adminbot.vaadin.mailings.presenter.MailingViewCallback;
import team.mephi.adminbot.vaadin.mailings.tabs.MailingTabProvider;

import java.util.*;


@Route(value = "/mailings", layout = DialogsLayout.class)
@RolesAllowed("ADMIN")
public class Mailings extends VerticalLayout implements MailingViewCallback {
    private final TabSheet tabSheet = new TabSheet();
    private final Button primaryButton = new Button(getTranslation("page_mailing_create_mailing_button"), new Icon(VaadinIcon.PLUS));

    private final MailingEditorDialog mailingEditorDialog;
    private final TemplateEditorDialog templateEditorDialog;
    private final SimpleConfirmDialog dialogMailingDelete;
    private final SimpleConfirmDialog dialogTemplateDelete;
    private final SimpleConfirmDialog dialogCancel;
    private final SimpleConfirmDialog dialogRetry;

    private final List<String> rolesInOrder = new ArrayList<>();
    private final Map<String, CRUDActions> actions = new HashMap<>();

    public Mailings(
            List<MailingTabProvider> tabProviders,
            MailingPresenterFactory presenterFactory,
            MailingEditorDialogFactory mailingDialogFactory,
            TemplateEditorDialogFactory templateDialogFactory,
            MailingCountService mailingCountService
    ) {
        this.dialogMailingDelete = new SimpleConfirmDialog(
                "dialog_delete_mailing_title", "dialog_delete_mailing_text", "dialog_delete_mailing_action",
                "dialog_delete_mailing_all_title", "dialog_delete_mailing_all_text"
        );
        this.dialogTemplateDelete = new SimpleConfirmDialog(
                "dialog_delete_template_title", "dialog_delete_template_text", "dialog_delete_template_action",
                "dialog_delete_template_all_title", "dialog_delete_template_all_text"
        );
        this.dialogCancel = new SimpleConfirmDialog(
                "dialog_cancel_mailing_title", "dialog_cancel_mailing_text", "dialog_cancel_mailing_action"
        );
        this.dialogRetry = new SimpleConfirmDialog(
                "dialog_retry_mailing_title", "dialog_retry_mailing_text", "dialog_retry_mailing_action"
        );
        this.mailingEditorDialog = mailingDialogFactory.create();
        this.templateEditorDialog = templateDialogFactory.create();

        setHeightFull();
        tabSheet.setSizeFull();
        add(createHeader(), tabSheet, mailingEditorDialog);

        tabProviders.sort(Comparator.comparingInt(MailingTabProvider::getPosition));

        for (var provider : tabProviders) {
            var tabId = provider.getTabId();
            var presenter = presenterFactory.createPresenter(tabId, this);
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
        return actions.get(getCurrentRole());
    }

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
        Notification.show(getTranslation("notification_mailing_cancel"), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForRetry(Long id) {
        Notification.show(getTranslation("notification_mailing_retry"), 3000, Notification.Position.TOP_END);
    }

    @Override
    public Object getEditedItem() {
        if (getCurrentRole().equals("templates")) {
            return templateEditorDialog.getEditedItem();
        } else {
            return mailingEditorDialog.getEditedItem();
        }
    }

    @Override
    public void showDialogForView(SimpleMailing user) {

    }

    @Override
    public void showDialogForNew(String role, SerializableRunnable callback) {
        if (getCurrentRole().equals("templates")) {
            templateEditorDialog.setHeaderTitle("dialog_template_new_title");
            templateEditorDialog.showDialogForNew(callback);
        } else {
            mailingEditorDialog.setHeaderTitle("dialog_mailing_new_title");
            mailingEditorDialog.showDialogForNew(callback);
        }
    }

    @Override
    public void showDialogForEdit(Object mailing, SerializableRunnable callback) {
        if (getCurrentRole().equals("templates")) {
            templateEditorDialog.setHeaderTitle("dialog_template_edit_title");
            templateEditorDialog.showDialogForEdit((SimpleTemplate) mailing, callback);
        } else {
            mailingEditorDialog.setHeaderTitle("dialog_mailing_edit_title");
            mailingEditorDialog.showDialogForEdit((SimpleMailing) mailing, callback);
        }
    }

    @Override
    public void confirmDelete(List<Long> ids, Runnable onConfirm) {
        if (getCurrentRole().equals("templates")) {
            dialogTemplateDelete.showForConfirm(ids.size(), onConfirm);
        } else {
            dialogMailingDelete.showForConfirm(ids.size(), onConfirm);
        }
    }

    @Override
    public void showNotificationForNew() {
        Notification.show(getTranslation("notification_mailing_created"), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForEdit(Long id) {
        Notification.show(getTranslation("notification_mailing_saved"), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForDelete(List<Long> ids) {
        if (getCurrentRole().equals("templates")) {
            String message = getTranslation("notification_template_delete");
            if (ids.size() > 1) {
                message = getTranslation("notification_template_delete_all", ids.size());
            }
            Notification.show(message, 3000, Notification.Position.TOP_END);
        } else {
            String message = getTranslation("notification_mailing_delete");
            if (ids.size() > 1) {
                message = getTranslation("notification_mailing_delete_all", ids.size());
            }
            Notification.show(message, 3000, Notification.Position.TOP_END);
        }
    }
}
