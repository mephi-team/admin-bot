package team.mephi.adminbot.vaadin.users.service;

import com.vaadin.flow.component.notification.Notification;
import team.mephi.adminbot.vaadin.components.UserConfirmDialog;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.components.UserEditorDialog;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;

import java.util.List;

public class UsersPresenter implements UserActions {

    private final UserEditorDialog editorDialog;
    private final UserConfirmDialog dialogBlock;
    private final UserConfirmDialog dialogAccept;
    private final UserConfirmDialog dialogReject;
    private final UserDataProvider dataProvider;

    // --- Константы текстов ---
    private static final String BLOCK_TITLE = "Блокировать пользователя?";
    private static final String BLOCK_TEXT = "Вы действительно хотите заблокировать пользователя?";
    private static final String BLOCK_MESSAGE = "Пользователь заблокирован";
    private static final String BLOCK_ALL_TITLE = "Блокировать пользователей?";
    private static final String BLOCK_ALL_TEXT = "Вы действительно хотите заблокировать %d пользователей?";
    private static final String BLOCK_ALL_MESSAGE = "Заблокировано %d пользователей";
    private static final String BLOCK_ACTION = "Блокировать";

    private static final String ACCEPT_TITLE = "Утвердить кандидата?";
    private static final String ACCEPT_TEXT = "Вы действительно хотите утвердить кандидата?";
    private static final String ACCEPT_MESSAGE = "Информация о приеме отправлена";
    private static final String ACCEPT_ALL_TITLE = "Утвердить кандидатов?";
    private static final String ACCEPT_ALL_TEXT = "Вы действительно хотите утвердить %d кандидатов?";
    private static final String ACCEPT_ALL_MESSAGE = "Информация о приеме отправлена %d кандидатам";
    private static final String ACCEPT_ACTION = "Утвердить";

    private static final String REJECT_TITLE = "Отклонить кандидата?";
    private static final String REJECT_TEXT = "Вы действительно хотите отклонить кандидата?";
    private static final String REJECT_MESSAGE = "Информация об отказе отправлена кандидату";
    private static final String REJECT_ALL_TITLE = "Отклонить кандидатов?";
    private static final String REJECT_ALL_TEXT = "Вы действительно хотите отклонить %d кандидатов?";
    private static final String REJECT_ALL_MESSAGE = "Информация об отказе отправлена %d кандидатам";
    private static final String REJECT_ACTION = "Отклонить";

    public UsersPresenter(
            UserEditorDialog editorDialog,
            UserDataProvider dataProvider
    ) {
        this.editorDialog = editorDialog;
        this.dataProvider = dataProvider;

        this.dialogBlock = new UserConfirmDialog(
                BLOCK_TITLE, BLOCK_TEXT, BLOCK_ACTION,
                BLOCK_ALL_TITLE, String.format(BLOCK_ALL_TEXT, 0), // placeholder
                null
        );
        this.dialogAccept = new UserConfirmDialog(
                ACCEPT_TITLE, ACCEPT_TEXT, ACCEPT_ACTION,
                ACCEPT_ALL_TITLE, String.format(ACCEPT_ALL_TEXT, 0),
                null
        );
        this.dialogReject = new UserConfirmDialog(
                REJECT_TITLE, REJECT_TEXT, REJECT_ACTION,
                REJECT_ALL_TITLE, String.format(REJECT_ALL_TEXT, 0),
                null
        );
    }

    // --- Реализация UserActions ---

    @Override
    public void onView(Long id) {
        dataProvider.findUserById(id).ifPresent(editorDialog::openForView);
    }

    @Override
    public void onEdit(Long id) {
        dataProvider.findUserById(id).ifPresent(u -> {
            editorDialog.openForEdit(u);
            editorDialog.setOnSaveCallback(() -> {
                dataProvider.save(editorDialog.getEditedUser());
                dataProvider.refresh();
            });
        });
    }

    @Override
    public void onDelete(List<Long> ids) {
        dialogBlock.setCount(ids.size());
        dialogBlock.setOnConfirm(() -> {
            dataProvider.deleteAllById(ids);
            dataProvider.refresh();
            showNotification(BLOCK_MESSAGE, BLOCK_ALL_MESSAGE, ids.size());
        });
        dialogBlock.open();
    }

    @Override
    public void onAccept(List<Long> ids) {
        dialogAccept.setCount(ids.size());
        dialogAccept.setOnConfirm(() -> {
            showNotification(ACCEPT_MESSAGE, ACCEPT_ALL_MESSAGE, ids.size());
        });
        dialogAccept.open();
    }

    @Override
    public void onReject(List<Long> ids) {
        dialogReject.setCount(ids.size());
        dialogReject.setOnConfirm(() -> {
            showNotification(REJECT_MESSAGE, REJECT_ALL_MESSAGE, ids.size());
        });
        dialogReject.open();
    }

    private void showNotification(String single, String plural, int count) {
        if (count == 1) {
            Notification.show(single, 3000, Notification.Position.TOP_END);
        } else if (count > 1) {
            Notification.show(String.format(plural, count), 3000, Notification.Position.TOP_END);
        }
    }
}