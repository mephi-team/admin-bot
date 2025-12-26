package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
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
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.components.SimpleConfirmDialog;
import team.mephi.adminbot.vaadin.components.UserCountBadge;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.components.TutoringDialog;
import team.mephi.adminbot.vaadin.users.components.TutoringDialogFactory;
import team.mephi.adminbot.vaadin.users.components.UserEditorDialog;
import team.mephi.adminbot.vaadin.users.components.UserEditorDialogFactory;
import team.mephi.adminbot.vaadin.users.service.*;
import team.mephi.adminbot.vaadin.users.tabs.UserTabProvider;

import java.util.*;

@Route("/users")
@RolesAllowed("ADMIN")
public class Users extends VerticalLayout implements UserViewCallback {
    private final UserEditorDialog editorDialog;
    private final TutoringDialog tutoringDialog;
    private final SimpleConfirmDialog dialogBlock;
    private final SimpleConfirmDialog dialogAccept;
    private final SimpleConfirmDialog dialogReject;

    private final TabSheet tabSheet = new TabSheet();
    private final List<String> rolesInOrder = new ArrayList<>();
    private final Map<String, CRUDActions> actions = new HashMap<>();

    private static final UserActions NO_OP_ACTIONS = new UserActions() {
        @Override public void onCreate(String role) {}
        @Override public void onView(Long id) {}
        @Override public void onEdit(Long id) {}
        @Override public void onDelete(List<Long> ids) {}
        @Override public void onAccept(List<Long> ids) {}
        @Override public void onReject(List<Long> ids) {}
        @Override public void onBlock(List<Long> ids) {}
    };

    public Users(
            List<UserTabProvider> tabProviders,
            UsersPresenterFactory presenterFactory,
            UserEditorDialogFactory dialogFactory,
            UserCountService userCountService,
            TutoringDialogFactory tutoringDialogFactory
    ) {
        this.editorDialog = dialogFactory.create();
        this.tutoringDialog = tutoringDialogFactory.create();

        this.dialogBlock = new SimpleConfirmDialog(
                "dialog_block_users_title", "dialog_block_users_text", "dialog_block_users_action",
                "dialog_block_users_all_title", "dialog_block_users_all_text"
        );
        this.dialogAccept = new SimpleConfirmDialog(
                "dialog_accept_users_title", "dialog_accept_users_text", "dialog_accept_users_action",
                "dialog_accept_users_all_title", "dialog_accept_users_all_text"
        );
        this.dialogReject = new SimpleConfirmDialog(
                "dialog_reject_users_title", "dialog_reject_users_text", "dialog_reject_users_action",
                "dialog_reject_users_all_title", "dialog_reject_users_all_text"
        );

        setSizeFull();
        tabSheet.setSizeFull();
        add(createHeader(), tabSheet, editorDialog);

        tabProviders.sort(Comparator.comparingInt(UserTabProvider::getPosition));

        // Создаём вкладки
        for (var provider : tabProviders) {
            var tabId = provider.getTabId();
            var dataProvider = presenterFactory.createDataProvider(tabId);
            CRUDPresenter<?> presenter;
            if (tabId.equals("tutor")) {
                presenter = new TutorPresenter(dataProvider, new TutorViewCallback() {
                    @Override
                    public void setOnSaveCallback(SerializableRunnable callback) {
                        editorDialog.setOnSaveCallback(callback);
                    }
                    @Override
                    public SimpleUser getEditedItem() {
                        return editorDialog.getEditedUser();
                    }
                    @Override
                    public void showDialogForView(SimpleUser user) {
                        editorDialog.openForView(user);
                    }
                    @Override
                    public void showDialogForNew(String role) {
                        editorDialog.setHeaderTitle("dialog_users_new_title");
                        editorDialog.openForNew(role);
                    }
                    @Override
                    public void showDialogForEdit(SimpleUser user) {
                        editorDialog.setHeaderTitle("dialog_users_edit_title");
                        editorDialog.openForEdit(user);
                    }
                    @Override
                    public void confirmDelete(List<Long> ids, Runnable onConfirm) {
                        dialogBlock.showForConfirm(ids.size(), onConfirm);
                    }
                    @Override
                    public void showNotificationForNew() {
                        Notification.show(getTranslation("notification_users_created"), 3000, Notification.Position.TOP_END);
                    }
                    @Override
                    public void showNotificationForEdit(Long id) {
                        Notification.show(getTranslation("notification_users_created"), 3000, Notification.Position.TOP_END);
                    }
                    @Override
                    public void showNotificationForDelete(List<Long> ids) {
                        Notification.show(makeNotification("notification_users_blocked", "notification_users_blocked_all", ids.size()), 3000, Notification.Position.TOP_END);
                    }

                    @Override
                    public void showDialogForTutoring(SimpleUser user) {
                        tutoringDialog.openForView(user);
                    }
                    @Override
                    public void showNotificationForTutoring(Long id) {
                        Notification.show("Test", 3000, Notification.Position.TOP_END);
                    }
                });
            } else {
                presenter = new UsersPresenter(dataProvider, this);
            }

            var content = provider.createTabContent(dataProvider, presenter);

            rolesInOrder.add(tabId);
            actions.put(tabId, presenter);

            var userCount = userCountService.getAllCounts().getOrDefault(provider.getTabId(), 0L);
            Span tabContent = new Span(new Span(getTranslation(provider.getTabLabel())), new UserCountBadge(userCount));
            tabSheet.add(new Tab(tabContent), content, provider.getPosition());
        }
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.addToStart(new H1(getTranslation("page_users_title")));

        var primaryButton = new Button(getTranslation("page_users_create_user_button"), new Icon(VaadinIcon.PLUS), e -> {
            getCurrentAction().onCreate(getCurrentRole());
        });
        primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Div buttons = new Div(new Button(getTranslation("page_users_create_from_file_button"), new Icon(VaadinIcon.FILE_ADD)), primaryButton);
        buttons.getElement().getStyle().set("display", "flex");
        buttons.getElement().getStyle().set("gap", "24px");
        top.addToEnd(buttons);
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

    @Override
    public void setOnSaveCallback(SerializableRunnable callback) {
        editorDialog.setOnSaveCallback(callback);
    }

    @Override
    public SimpleUser getEditedItem() {
        return editorDialog.getEditedUser();
    }

    @Override
    public void showDialogForView(SimpleUser user) {
        editorDialog.setHeaderTitle("dialog_users_view_title");
        editorDialog.openForView(user);
    }

    @Override
    public void showDialogForEdit(SimpleUser user) {
        editorDialog.setHeaderTitle("dialog_users_edit_title");
        editorDialog.openForEdit(user);
    }

    @Override
    public void showDialogForNew(String role) {
        editorDialog.setHeaderTitle("dialog_users_new_title");
        editorDialog.openForNew(role);
    }

    @Override
    public void confirmDelete(List<Long> ids, Runnable onConfirm) {
        dialogBlock.showForConfirm(ids.size(), onConfirm);
    }

    @Override
    public void showNotificationForNew() {
        Notification.show(getTranslation("notification_users_created"), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForEdit(Long id) {
        Notification.show(getTranslation("notification_users_saved"), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForDelete(List<Long> ids) {
        Notification.show(makeNotification("notification_users_blocked", "notification_users_blocked_all", ids.size()), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void confirmAccept(List<Long> ids, Runnable onConfirm) {
        dialogAccept.showForConfirm(ids.size(), onConfirm);
    }

    @Override
    public void confirmReject(List<Long> ids, Runnable onConfirm) {
        dialogReject.showForConfirm(ids.size(), onConfirm);
    }

    @Override
    public void showNotificationForAccept(List<Long> ids) {
        Notification.show(makeNotification("notification_users_accepted", "notification_users_accepted_all", ids.size()), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForReject(List<Long> ids) {
        Notification.show(makeNotification("notification_users_rejected", "notification_users_rejected_all", ids.size()), 3000, Notification.Position.TOP_END);
    }

    private String makeNotification(String single, String plural, int count) {
        if (count > 1) {
            return getTranslation(plural, count);
        }
        return getTranslation(single);
    }
}
